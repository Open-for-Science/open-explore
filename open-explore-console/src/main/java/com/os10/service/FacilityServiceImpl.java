package com.os10.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.os10.common.support.ESResult;
import com.os10.common.support.EsFacilityService;
import com.os10.entity.FacilityDO;
import com.os10.dto.FacilityQuery;
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
 * 大型科学装置业务逻辑实现
 */
@Service
public class FacilityServiceImpl {

    @Autowired
    private EsFacilityService esFacilityService;

    /**
     * 创建/编辑装置条目
     */
    public BaseMessage editFacility(FacilityDO facilityDO) {
        String id = facilityDO.getUuid();
        if (StrUtil.isEmpty(id)) {
            id = UUID.randomUUID().toString().replace("-", "");
            facilityDO.setUuid(id);
            JSONObject index = esFacilityService.index(Collections.singletonList(JSONObject.parseObject(JSONObject.toJSONString(facilityDO))));
            return BaseMessage.success(index);
        } else {
            JSONObject update = esFacilityService.updateById(id, JSONObject.parseObject(JSONObject.toJSONString(facilityDO)));
            return BaseMessage.success(update);
        }
    }

    /**
     * 分页查询装置列表
     */
    public BaseMessage listFacility(FacilityQuery facilityQuery) {
        ESResult query = esFacilityService.query(searchSourceBuilder -> {
            String orderField = facilityQuery.getOrderField();
            if (StrUtil.isEmpty(orderField)) {
                orderField = "_score";
            }
            String orderDirection = facilityQuery.getOrderDirection();
            if (StrUtil.isEmpty(orderDirection)) {
                orderDirection = "desc";
            }
            String pageStr = facilityQuery.getPage();
            if (StrUtil.isEmpty(pageStr)) {
                pageStr = "1";
            }
            int page = 1;
            try {
                page = Math.max(Integer.parseInt(pageStr), 1);
            } catch (Exception ignored) {}
            String sizeStr = facilityQuery.getSize();
            int size = 10;
            try {
                size = Math.max(Integer.parseInt(sizeStr), 1);
            } catch (Exception ignored) {}
            searchSourceBuilder.sort(orderField, SortOrder.valueOf(orderDirection.toUpperCase(Locale.ROOT)));
            searchSourceBuilder.from((page - 1) * size).size(size);

            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            buildFilterQuery(boolQueryBuilder, facilityQuery);
            searchSourceBuilder.query(boolQueryBuilder);
        });

        List<JSONObject> jsonList = esFacilityService.getJsonList(query.getHits());
        JSONObject result = new JSONObject();
        result.put("content", jsonList);
        result.put("total", query.getTotal());
        return BaseMessage.success(result);
    }

    /**
     * 装置聚合分析
     */
    public BaseMessage<JSONObject> listFacilityAggregations(FacilityQuery facilityQuery) {
        ESResult query = esFacilityService.query(searchSourceBuilder -> {
            Map<String, String> aggregations = facilityQuery.getAggregations();
            if (CollectionUtil.isEmpty(aggregations)) {
                aggregations = new HashMap<>();
                aggregations.put("facility-type", "");
                aggregations.put("facility-status", "");
                aggregations.put("pub-year", "");
                aggregations.put("facility-location", "");
            }
            searchSourceBuilder.size(0);
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            buildFilterQuery(boolQueryBuilder, facilityQuery);
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
     * 根据 uuid 获取单条装置
     */
    public BaseMessage getFacility(String id) {
        JSONObject data = esFacilityService.queryById(id);
        return BaseMessage.success(data);
    }

    /**
     * 获取最新装置（首页展示用）
     */
    public BaseMessage listLatestFacility(int size) {
        ESResult query = esFacilityService.query(searchSourceBuilder -> {
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            searchSourceBuilder.sort("pub-date", SortOrder.DESC);
            searchSourceBuilder.from(0).size(size);
        });
        List<JSONObject> jsonList = esFacilityService.getJsonList(query.getHits());
        JSONObject result = new JSONObject();
        result.put("content", jsonList);
        result.put("total", query.getTotal());
        return BaseMessage.success(result);
    }

    /**
     * 获取装置总数
     */
    public long countTotal() {
        ESResult result = esFacilityService.query(searchSourceBuilder -> {
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            searchSourceBuilder.size(0);
        });
        return result != null ? result.getTotal() : 0;
    }

    public static String formatQuery(String value) {
        if (StrUtil.isNotEmpty(value)) {
            return value.replaceAll("[?:/\\\\]", "")
                    .replace("^", "\\^");
        }
        return value;
    }

    public void buildFilterQuery(BoolQueryBuilder searchQueryBuilder, FacilityQuery facilityQuery) {
        String query = formatQuery(facilityQuery.getQuery());
        String strategy = facilityQuery.getStrategy();

        Map<String, String> aggregations = facilityQuery.getAggregations();
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
