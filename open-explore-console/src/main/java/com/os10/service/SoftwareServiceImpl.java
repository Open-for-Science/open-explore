package com.os10.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.os10.common.support.ESResult;
import com.os10.common.support.EsSoftwareService;
import com.os10.entity.SoftwareDO;
import com.os10.dto.SoftwareQuery;
import com.os10.util.BaseMessage;
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
 * 科学软件业务逻辑实现
 */
@Service
public class SoftwareServiceImpl {

    @Autowired
    private EsSoftwareService esSoftwareService;

    /**
     * 创建/编辑软件条目
     */
    public BaseMessage editSoftware(SoftwareDO softwareDO) {
        String id = softwareDO.getUuid();
        if (StrUtil.isEmpty(id)) {
            // 新增：自动生成 uuid
            id = UUID.randomUUID().toString().replace("-", "");
            softwareDO.setUuid(id);
            JSONObject index = esSoftwareService.index(Collections.singletonList(JSONObject.parseObject(JSONObject.toJSONString(softwareDO))));
            return BaseMessage.success(index);
        } else {
            // 更新
            JSONObject update = esSoftwareService.updateById(id, JSONObject.parseObject(JSONObject.toJSONString(softwareDO)));
            return BaseMessage.success(update);
        }
    }

    /**
     * 分页查询软件列表
     */
    public BaseMessage listSoftware(SoftwareQuery softwareQuery) {
        ESResult query = esSoftwareService.query(searchSourceBuilder -> {
            String orderField = softwareQuery.getOrderField();
            if (StrUtil.isEmpty(orderField)) {
                orderField = "_score";
            }
            String orderDirection = softwareQuery.getOrderDirection();
            if (StrUtil.isEmpty(orderDirection)) {
                orderDirection = "desc";
            }
            String pageStr = softwareQuery.getPage();
            if (StrUtil.isEmpty(pageStr)) {
                pageStr = "1";
            }
            int page = 1;
            try {
                page = Math.max(Integer.parseInt(pageStr), 1);
            } catch (Exception ignored) {}
            String sizeStr = softwareQuery.getSize();
            int size = 10;
            try {
                size = Math.max(Integer.parseInt(sizeStr), 1);
            } catch (Exception ignored) {}
            searchSourceBuilder.sort(orderField, SortOrder.valueOf(orderDirection.toUpperCase(Locale.ROOT)));
            searchSourceBuilder.from((page - 1) * size).size(size);

            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            buildFilterQuery(boolQueryBuilder, softwareQuery);
            searchSourceBuilder.query(boolQueryBuilder);
        });

        List<JSONObject> mappedList = new ArrayList<>();
        for (JSONObject item : esSoftwareService.getJsonList(query.getHits())) {
            mappedList.add(mapSoftwareFields(item));
        }
        JSONObject result = new JSONObject();
        result.put("content", mappedList);
        result.put("total", query.getTotal());
        return BaseMessage.success(result);
    }

    /**
     * 将索引文档转换为前端可读字段（列表/详情/首页复用）
     * 索引字段：name / description / tag / publish_time / pub_year / category / resource_type / language_main ...
     */
    private JSONObject mapSoftwareFields(JSONObject item) {
        JSONObject mapped = new JSONObject();
        String name = item.getString("name");
        if (StrUtil.isEmpty(name)) {
            name = item.getString("title");
        }
        mapped.put("uuid", item.getString("uuid"));
        mapped.put("title", StrUtil.isEmpty(name) ? "" : name);

        String description = item.getString("description");
        mapped.put("abstracts", StrUtil.isEmpty(description) ? "" : description);
        mapped.put("description", StrUtil.isEmpty(description) ? "" : description);

        JSONArray tags = item.getJSONArray("tag");
        mapped.put("keyword", tags != null ? tags : new JSONArray());
        JSONArray authors = item.getJSONArray("author_name");
        mapped.put("author", authors != null ? authors : new JSONArray());

        String pubDate = item.getString("publish_time");
        String pubYear = item.getString("pub_year");
        mapped.put("pub-date", pubDate != null ? pubDate : "");
        mapped.put("pub-year", pubYear != null ? pubYear : "");
        mapped.put("date", pubDate != null ? pubDate : "");
        mapped.put("year", pubYear != null ? pubYear : "");

        mapped.put("category", nvl(item.getString("category")));
        mapped.put("resource-type", nvl(item.getString("resource_type")));
        mapped.put("programming-language", nvl(item.getString("language_main")));
        mapped.put("license", nvl(item.getString("license")));
        mapped.put("status", nvl(item.getString("status")));
        String org = item.getString("organization");
        if (StrUtil.isEmpty(org)) {
            org = item.getString("aggregated_source");
        }
        mapped.put("organization", nvl(org));
        mapped.put("source-url", nvl(item.getString("homepage")));
        mapped.put("detail_url", nvl(item.getString("detail_url")));
        mapped.put("image-url", "");
        mapped.put("version", nvl(item.getString("software_version")));
        mapped.put("doi", "");
        mapped.put("journal", "");
        mapped.put("link", "");
        return mapped;
    }

    private String nvl(String value) {
        return value != null ? value : "";
    }

    /**
     * 软件聚合分析
     */
    public BaseMessage<JSONObject> listSoftwareAggregations(SoftwareQuery softwareQuery) {
        ESResult query = esSoftwareService.query(searchSourceBuilder -> {
            Map<String, String> aggregations = softwareQuery.getAggregations();
            if (CollectionUtil.isEmpty(aggregations)) {
                aggregations = new HashMap<>();
                aggregations.put("resource_type", "");
                aggregations.put("category", "");
                aggregations.put("language_main", "");
                aggregations.put("license", "");
                aggregations.put("pub_year", "");
            }
            searchSourceBuilder.size(0);
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            buildFilterQuery(boolQueryBuilder, softwareQuery);
            searchSourceBuilder.query(boolQueryBuilder);

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
     * 根据 uuid 获取单条软件
     */
    public BaseMessage getSoftware(String id) {
        JSONObject data = esSoftwareService.queryById(id);
        if (data != null && !data.isEmpty()) {
            return BaseMessage.success(mapSoftwareFields(data));
        }
        return BaseMessage.success(data);
    }

    /**
     * 获取最新的软件（首页展示用）
     */
    public BaseMessage listLatestSoftware(int size) {
        ESResult query = esSoftwareService.query(searchSourceBuilder -> {
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            searchSourceBuilder.sort("publish_time", SortOrder.DESC);
            searchSourceBuilder.from(0).size(size);
        });
        List<JSONObject> mappedList = new ArrayList<>();
        for (JSONObject item : esSoftwareService.getJsonList(query.getHits())) {
            mappedList.add(mapSoftwareFields(item));
        }
        JSONObject result = new JSONObject();
        result.put("content", mappedList);
        result.put("total", query.getTotal());
        return BaseMessage.success(result);
    }

    /**
     * 获取软件总数
     */
    public long countTotal() {
        ESResult result = esSoftwareService.query(searchSourceBuilder -> {
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            searchSourceBuilder.size(0);
        });
        return result != null ? result.getTotal() : 0;
    }

    /**
     * 格式化查询字符串
     */
    public static String formatQuery(String value) {
        if (StrUtil.isNotEmpty(value)) {
            return value.replaceAll("[?:/\\\\]", "")
                    .replace("^", "\\^");
        }
        return value;
    }

    public void buildFilterQuery(BoolQueryBuilder searchQueryBuilder, SoftwareQuery softwareQuery) {
        String query = formatQuery(softwareQuery.getQuery());
        String strategy = softwareQuery.getStrategy();

        Map<String, String> aggregations = softwareQuery.getAggregations();
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
                searchShould = QueryBuilders.boolQuery();
                searchShould.should(QueryBuilders.queryStringQuery(strategy)
                        .defaultOperator(QueryStringQueryBuilder.Operator.AND)
                        .phraseSlop(0)
                        .autoGeneratePhraseQueries(true));
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
                searchShould = QueryBuilders.boolQuery();
                QueryStringQueryBuilder queryStringQuery = QueryBuilders.queryStringQuery(query)
                        .defaultOperator(QueryStringQueryBuilder.Operator.AND)
                        .autoGeneratePhraseQueries(true)
                        .field("title_ik", 150.0F)
                        .field("abstracts_ik")
                        .field("keyword_ik")
                        .phraseSlop(0);
                searchShould.should(queryStringQuery);
                searchShould.should(QueryBuilders.matchPhraseQuery("title_ik", query).boost(20.0F));
                searchQueryBuilder.must(searchShould);
            }
        }
    }
}
