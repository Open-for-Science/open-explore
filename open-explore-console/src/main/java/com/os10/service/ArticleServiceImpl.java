package com.os10.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.os10.common.support.ESResult;
import com.os10.common.support.EsArticleService;
import com.os10.entity.ArticleDO;
import com.os10.dto.ArticleQuery;
import com.os10.util.BaseMessage;
import com.os10.util.MD5Util;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 文章业务逻辑实现
 */
@Service
public class ArticleServiceImpl {

    @Autowired
    private EsArticleService esArticleService;

    /**
     * 创建/编辑文章
     * 新增：基于 title+date+journal MD5 生成 uuid，重复时拒绝
     * 更新：已有 uuid 则覆盖式更新
     */
    public BaseMessage editArticle(ArticleDO articleDO) {
        String title = articleDO.getTitle();
        String id = articleDO.getUuid();
        String date = articleDO.getDate();
        String journal = articleDO.getJournal();

        // 从日期中提取年份
        if (StrUtil.isNotEmpty(date)) {
            articleDO.setYear(date.substring(0, 4));
        }
        if (StrUtil.isEmpty(id)) {
            // ---- 新增 ----
            if (StrUtil.isEmpty(title) || StrUtil.isEmpty(date) || StrUtil.isEmpty(journal)) {
                return BaseMessage.paramsEmpty();
            }
            id = MD5Util.getMD5(title + date + journal);
            // 检查是否已存在（重复校验）
            JSONObject data = (JSONObject) getArticle(id).getData();
            if (CollectionUtil.isNotEmpty(data)) {
                return BaseMessage.error(-1, "重复添加!");
            }
            articleDO.setUuid(id);
            JSONObject index = esArticleService.index(Collections.singletonList(JSONObject.parseObject(JSONObject.toJSONString(articleDO))));
            return BaseMessage.success(index);
        } else {
            // ---- 更新 ----
            JSONObject update = esArticleService.updateById(id, JSONObject.parseObject(JSONObject.toJSONString(articleDO)));
            return BaseMessage.success(update);
        }
    }

    /**
     * 分页查询文章列表
     * 支持全文检索（title_ik/abstracts_ik/keyword_ik 走 IK 分词）、分面过滤、排序、分页
     */
    public BaseMessage listArticles(ArticleQuery articleQuery) {
        ESResult query = esArticleService.query(searchSourceBuilder -> {
            // 排序
            String orderField = articleQuery.getOrderField();
            if (StrUtil.isEmpty(orderField)) {
                orderField = "_score";
            }
            String orderDirection = articleQuery.getOrderDirection();
            if (StrUtil.isEmpty(orderDirection)) {
                orderDirection = "desc";
            }
            // 分页
            String pageStr = articleQuery.getPage();
            if (StrUtil.isEmpty(pageStr)) {
                pageStr = "1";
            }
            int page = 1;
            try {
                page = Math.max(Integer.parseInt(pageStr), 1);
            } catch (Exception ignored) {
            }
            String sizeStr = articleQuery.getSize();
            int size = 10;
            try {
                size = Math.max(Integer.parseInt(sizeStr), 1);
            } catch (Exception e) {
            }
            searchSourceBuilder.sort(orderField, (SortOrder.valueOf(orderDirection.toUpperCase(Locale.ROOT))));
            searchSourceBuilder.from((page - 1) * size).size(size);

            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            buildFilterQuery(boolQueryBuilder, articleQuery);
            searchSourceBuilder.query(boolQueryBuilder);
        });

        JSONObject result = new JSONObject();
        List<JSONObject> jsonList = esArticleService.getJsonList(query.getHits());
        result.put("content", jsonList);
        result.put("total", query.getTotal());
        return BaseMessage.success(result);
    }

    /**
     * 文章聚合分析
     * 支持 subject / journal / year / collection / type 等字段的 terms 聚合
     * 可在请求中通过 aggregations 参数自定义聚合字段及过滤值
     */
    public BaseMessage<JSONObject> listArticleAggregations(ArticleQuery articleQuery) {
        ESResult query = esArticleService.query(searchSourceBuilder -> {
            Map<String, String> aggregations = articleQuery.getAggregations();
            if (CollectionUtil.isEmpty(aggregations)) {
                aggregations = new HashMap<>();
                aggregations.put("subject", "");
                aggregations.put("journal", "");
                aggregations.put("year", "");
                aggregations.put("collection", "");
                aggregations.put("type", "");
            }
            searchSourceBuilder.size(0);
            // 分面过滤
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            buildFilterQuery(boolQueryBuilder, articleQuery);
            searchSourceBuilder.query(boolQueryBuilder);

            // 添加 terms 聚合
            if (CollectionUtil.isNotEmpty(aggregations)) {
                aggregations.keySet().forEach(key -> {
                    TermsBuilder termsBuilder = AggregationBuilders.terms(key)
                            .field(key)
                            .executionHint("map");
                    termsBuilder.size(200);
                    searchSourceBuilder.aggregation(termsBuilder);
                });
            }
        });
        return BaseMessage.success(query.getAggs());
    }

    /**
     * 根据 uuid 获取单篇文章
     */
    public BaseMessage getArticle(String id) {
        JSONArray hits = esArticleService.query(searchSourceBuilder -> {
            searchSourceBuilder.query(QueryBuilders.termQuery("uuid", id));
        }).getHits();
        List<JSONObject> jsonList = esArticleService.getJsonList(hits);
        JSONObject result = new JSONObject();
        if (CollectionUtil.isNotEmpty(jsonList)) {
            result = jsonList.get(0);
        }
        return BaseMessage.success(result);
    }

    /**
     * 格式化查询字符串，过滤掉特殊字符
     */
    public static String formatQuery(String value) {
        if (StrUtil.isNotEmpty(value)) {
            return value.replaceAll("[?:/\\\\]", "")
                    .replace("^", "\\^");
        }
        return value;
    }

    /**
     * 构建 ES Bool 过滤查询
     * 支持两种搜索模式：
     * 1) strategy：精确策略搜索，支持 title:xxx 语法（query_string 模式）
     * 2) query：全文检索，走 IK 中文分词，匹配 title_ik / abstracts_ik / keyword_ik 字段
     * 同时支持聚合分面过滤（aggregations 中 value 不为空的字段做 filter）
     */
    /**
     * 获取最近发布的文章（用于首页展示）
     * 按 date 倒序取前 n 条
     */
    public BaseMessage listLatestArticles(int size) {
        ESResult query = esArticleService.query(searchSourceBuilder -> {
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            searchSourceBuilder.sort("date", SortOrder.DESC);
            searchSourceBuilder.from(0).size(size);
        });
        List<JSONObject> jsonList = esArticleService.getJsonList(query.getHits());
        for (JSONObject jsonObject : jsonList) {
            System.out.println(jsonObject.toJSONString());
        }
        JSONObject result = new JSONObject();
        result.put("content", jsonList);
        result.put("total", query.getTotal());
        return BaseMessage.success(result);
    }

    /**
     * 获取文章总数
     */
    public long countTotal() {
        ESResult result = esArticleService.query(searchSourceBuilder -> {
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            searchSourceBuilder.size(0);
        });
        return result != null ? result.getTotal() : 0;
    }

    public void buildFilterQuery(BoolQueryBuilder searchQueryBuilder, ArticleQuery articleQuery) {

        String query = formatQuery(articleQuery.getQuery());
        String strategy = articleQuery.getStrategy();

        // 分面过滤：当聚合字段有选中值时，对结果集做 filter
        Map<String, String> aggregations = articleQuery.getAggregations();
        if (CollectionUtil.isNotEmpty(aggregations)) {
            aggregations.forEach((key, value) -> {
                if (StringUtils.hasText(value)) {
                    searchQueryBuilder.filter(QueryBuilders.termsQuery(key, value.split(",")));
                }
            });
        }

        if (StrUtil.isNotEmpty(query) || StrUtil.isNotEmpty(strategy)) {
            BoolQueryBuilder searchShould;
            if (StrUtil.isNotEmpty(strategy)) {
                // ---- 精确策略搜索模式 ----
                searchShould = QueryBuilders.boolQuery();
                searchShould.should(QueryBuilders.queryStringQuery(strategy)
                        .defaultOperator(QueryStringQueryBuilder.Operator.AND)
                        .phraseSlop(0)
                        .autoGeneratePhraseQueries(true));
                // 解析 field:value 语法并做精确短语匹配
                String advancedSearchNew = strategy.replaceAll("[\\(\\)]", "");
                String[] oldSplit = strategy.split("\\:");
                String[] split = advancedSearchNew.split("\\:");
                if (split.length == 2) {
                    String newSearchText = oldSplit[1];
                    if (oldSplit[1].contains(")")) {
                        newSearchText = newSearchText.substring(0, oldSplit[1].lastIndexOf(")"));
                    }

                    searchShould.should(QueryBuilders.matchPhraseQuery(split[0], newSearchText).slop(0));
                }
                searchQueryBuilder.must(searchShould);
            } else {
                // ---- 全文检索模式（IK 中文分词） ----
                searchShould = QueryBuilders.boolQuery();
                QueryStringQueryBuilder queryStringQuery = QueryBuilders.queryStringQuery(query)
                        .defaultOperator(QueryStringQueryBuilder.Operator.AND)
                        .autoGeneratePhraseQueries(true)
                        .field("title_ik", 150.0F)   // 标题权重最高
                        .field("abstracts_ik")         // 摘要
                        .field("keyword_ik")           // 关键词
                        .phraseSlop(0);
                searchShould.should(queryStringQuery);
                // 标题精确短语匹配，额外提权
                searchShould.should(QueryBuilders.matchPhraseQuery("title_ik", query).boost(20.0F));
                searchQueryBuilder.must(searchShould);
            }
        }

    }

}
