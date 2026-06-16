package com.os10.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.os10.common.support.ESResult;
import com.os10.common.support.EsScienceDataService;
import com.os10.entity.ScienceDataDO;
import com.os10.dto.ScienceDataQuery;
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

@Service
public class ScienceDataServiceImpl {

    @Autowired
    private EsScienceDataService esScienceDataService;

    public BaseMessage editScienceData(ScienceDataDO scienceDataDO) {
        String title = scienceDataDO.getTitle();
        String id = scienceDataDO.getUuid();
        String pubDate = scienceDataDO.getPubDate();
        String doi = scienceDataDO.getDoi();

        if (StrUtil.isNotEmpty(pubDate) && pubDate.length() >= 4) {
            scienceDataDO.setPubYear(pubDate.substring(0, 4));
        }
        if (StrUtil.isEmpty(id)) {
            if (StrUtil.isEmpty(title) || StrUtil.isEmpty(pubDate)) {
                return BaseMessage.paramsEmpty();
            }
            String uniqueKey = title + pubDate + (doi != null ? doi : "");
            id = MD5Util.getMD5(uniqueKey);
            JSONObject data = (JSONObject)getScienceData(id).getData();
            if (CollectionUtil.isNotEmpty(data)) {
                return BaseMessage.error(-1, "重复添加!");
            }
            scienceDataDO.setUuid(id);
            JSONObject esDoc = convertToEsDocument(scienceDataDO);
            JSONObject index = esScienceDataService.index(Collections.singletonList(esDoc));
            return BaseMessage.success(index);
        } else {
            JSONObject esDoc = convertToEsDocument(scienceDataDO);
            JSONObject update = esScienceDataService.updateById(id, esDoc);
            return BaseMessage.success(update);
        }
    }

    /**
     * 将扁平的ScienceDataDO转换为ES嵌套文档结构(record/extend/process)
     */
    private JSONObject convertToEsDocument(ScienceDataDO scienceDataDO) {
        JSONObject esDoc = new JSONObject();

        // 顶层字段
        esDoc.put("uuid", scienceDataDO.getUuid());
        esDoc.put("is_display", scienceDataDO.getIsDisplay());
        esDoc.put("src-type", scienceDataDO.getSrcType());
        esDoc.put("src-data", scienceDataDO.getSrcData());
        esDoc.put("status", scienceDataDO.getStatus());
        esDoc.put("version", scienceDataDO.getVersion());
        esDoc.put("topic_list", scienceDataDO.getTopicList());
        esDoc.put("topic_black_list", scienceDataDO.getTopicBlackList());

        // record 字段
        JSONObject record = new JSONObject();
        record.put("title", scienceDataDO.getTitle());
        record.put("title-en", scienceDataDO.getTitleEn());
        record.put("abstract", scienceDataDO.getAbstracts());
        record.put("abstract-en", scienceDataDO.getAbstractEn());
        record.put("keyword", scienceDataDO.getKeyword());
        record.put("keyword-en", scienceDataDO.getKeywordEn());
        record.put("author", scienceDataDO.getAuthor());
        record.put("author-en", scienceDataDO.getAuthorEn());
        record.put("doi", scienceDataDO.getDoi());
        record.put("cstr", scienceDataDO.getCstr());
        record.put("pub-date", scienceDataDO.getPubDate());
        record.put("pub-year", scienceDataDO.getPubYear());
        record.put("dataset-id", scienceDataDO.getDatasetId());
        record.put("dataset-type", scienceDataDO.getDatasetType());
        record.put("type", scienceDataDO.getType());
        record.put("lang", scienceDataDO.getLang());
        record.put("size", scienceDataDO.getSize());
        record.put("file-count", scienceDataDO.getFileCount());
        record.put("record-count", scienceDataDO.getRecordCount());
        record.put("start-date", scienceDataDO.getStartDate());
        record.put("end-date", scienceDataDO.getEndDate());
        record.put("spatial-dimension", scienceDataDO.getSpatialDimension());
        record.put("facility", scienceDataDO.getFacility());
        record.put("group-name", scienceDataDO.getGroupName());
        record.put("provider-name", scienceDataDO.getProviderName());
        record.put("correspondent", scienceDataDO.getCorrespondent());
        record.put("uri", scienceDataDO.getUri());
        record.put("url", scienceDataDO.getUrl());
        record.put("image-url", scienceDataDO.getImageUrl());
        record.put("coverUrl", scienceDataDO.getCoverUrl());
        record.put("rights", scienceDataDO.getRights());
        record.put("share", scienceDataDO.getShare());
        record.put("reference", scienceDataDO.getReference());
        record.put("source-db", scienceDataDO.getSourceDb());
        record.put("pid", scienceDataDO.getPid());
        record.put("issue", scienceDataDO.getIssue());
        record.put("browse_count", scienceDataDO.getBrowseCount());
        record.put("download_count", scienceDataDO.getDownloadCount());
        record.put("classification", scienceDataDO.getClassification());
        record.put("contributor", scienceDataDO.getContributor());
        record.put("funding", scienceDataDO.getFunding());
        record.put("project", scienceDataDO.getProject());
        record.put("publisher", scienceDataDO.getPublisher());
        record.put("copyRight", scienceDataDO.getCopyRight());
        record.put("article", scienceDataDO.getArticle());
        esDoc.put("record", record);

        // extend 字段
        JSONObject extend = new JSONObject();
        extend.put("article-uuid", scienceDataDO.getArticleUuid());
        extend.put("book-uuid", scienceDataDO.getBookUuid());
        extend.put("patent-uuid", scienceDataDO.getPatentUuid());
        extend.put("project-uuid", scienceDataDO.getProjectUuid());
        extend.put("report-uuid", scienceDataDO.getReportUuid());
        extend.put("data-uuid", scienceDataDO.getDataUuid());
        extend.put("data-type", scienceDataDO.getDataType());
        extend.put("file-type", scienceDataDO.getFileType());
        extend.put("enrichment-article-uuid", scienceDataDO.getEnrichmentArticleUuid());
        extend.put("authority-scope", scienceDataDO.getAuthorityScope());
        extend.put("blockchain_id", scienceDataDO.getBlockchainId());
        extend.put("institution", scienceDataDO.getInstitution());
        extend.put("institution-en", scienceDataDO.getInstitutionEn());
        extend.put("contacts", scienceDataDO.getContacts());
        extend.put("supplement", scienceDataDO.getSupplement());
        esDoc.put("extend", extend);

        // process 字段
        JSONObject process = new JSONObject();
        process.put("insert-date", scienceDataDO.getInsertDate());
        process.put("insert-time", scienceDataDO.getInsertTime());
        process.put("insert-year", scienceDataDO.getInsertYear());
        process.put("update-date", scienceDataDO.getUpdateDate());
        process.put("update-time", scienceDataDO.getUpdateTime());
        process.put("update-year", scienceDataDO.getUpdateYear());
        process.put("operator", scienceDataDO.getOperator());
        process.put("source", scienceDataDO.getSource());
        esDoc.put("process", process);

        return esDoc;
    }

    public BaseMessage listScienceData(ScienceDataQuery scienceDataQuery){
        ESResult query = esScienceDataService.query(searchSourceBuilder -> {
            String orderField = scienceDataQuery.getOrderField();
            if (StrUtil.isEmpty(orderField)) {
                orderField = "_score";
            }
            String orderDirection = scienceDataQuery.getOrderDirection();
            if (StrUtil.isEmpty(orderDirection)) {
                orderDirection = "desc";
            }
            String pageStr = scienceDataQuery.getPage();
            if (StrUtil.isEmpty(pageStr)) {
                pageStr = "1";
            }
            int page = 1;
            try {
                page = Math.max(Integer.parseInt(pageStr), 1);
            } catch (Exception e) {
            }
            String sizeStr = scienceDataQuery.getSize();
            int size = 10;
            try {
                size = Math.max(Integer.parseInt(sizeStr), 1);
            } catch (Exception e) {
            }
            searchSourceBuilder.sort(orderField, (SortOrder.valueOf(orderDirection.toUpperCase(Locale.ROOT))));
            searchSourceBuilder.from((page - 1) * size).size(size);

            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            buildFilterQuery(boolQueryBuilder, scienceDataQuery);
            searchSourceBuilder.query(boolQueryBuilder);
        });

        JSONObject result = new JSONObject();
        List<JSONObject> jsonList = esScienceDataService.getJsonList(query.getHits());
        result.put("content", jsonList);
        result.put("total", query.getTotal());
        return BaseMessage.success(result);
    }

    public BaseMessage<JSONObject> listScienceDataAggregations(ScienceDataQuery scienceDataQuery){

        ESResult query = esScienceDataService.query(searchSourceBuilder -> {
            Map<String, String> aggregations = scienceDataQuery.getAggregations();
            if (CollectionUtil.isEmpty(aggregations)) {
                aggregations = new HashMap<>();
                // Based on ES index structure, common aggregation fields
                aggregations.put("record.classification.first", "");
                aggregations.put("record.classification.second", "");
                aggregations.put("record.pub-year", "");
                aggregations.put("record.dataset-type", "");
                aggregations.put("record.type", "");
            }
            searchSourceBuilder.size(0);
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            buildFilterQuery(boolQueryBuilder, scienceDataQuery);
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


    public BaseMessage getScienceData(String id){
        JSONArray hits = esScienceDataService.query(searchSourceBuilder -> {
            searchSourceBuilder.query(QueryBuilders.termQuery("uuid", id));
        }).getHits();
        List<JSONObject> jsonList = esScienceDataService.getJsonList(hits);
        JSONObject result = new JSONObject();
        if (CollectionUtil.isNotEmpty(jsonList)) {
            result = jsonList.get(0);
        }
        return BaseMessage.success(result);
    }

    public static String formatQuery(String value) {
        if (StrUtil.isNotEmpty(value)) {
            return value.replaceAll("[?:/\\\\]", "")
                    .replace("^", "\\^");
        }
        return value;
    }

    public void buildFilterQuery(BoolQueryBuilder searchQueryBuilder, ScienceDataQuery scienceDataQuery) {

        String query = formatQuery(scienceDataQuery.getQuery());
        String strategy = scienceDataQuery.getStrategy();

        // 分面请求
        Map<String, String> aggregations = scienceDataQuery.getAggregations();
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
                searchShould.should(QueryBuilders.queryStringQuery(strategy).defaultOperator(QueryStringQueryBuilder.Operator.AND).phraseSlop(0).autoGeneratePhraseQueries(true));
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
                // Search in title, abstract, and keyword fields with IK analyzer
                QueryStringQueryBuilder queryStringQuery = QueryBuilders.queryStringQuery(query)
                        .defaultOperator(QueryStringQueryBuilder.Operator.AND)
                        .autoGeneratePhraseQueries(true)
                        .field("record.title_ik", 150.0F)
                        .field("record.title-en_ik", 100.0F)
                        .field("record.abstract_ik")
                        .field("record.abstract-en_ik")
                        .field("record.keyword_ik")
                        .field("record.keyword-en_ik")
                        .phraseSlop(0);
                searchShould.should(queryStringQuery);
                searchShould.should(QueryBuilders.matchPhraseQuery("record.title_ik", query).boost(20.0F));
                searchShould.should(QueryBuilders.matchPhraseQuery("record.title-en_ik", query).boost(15.0F));
                searchQueryBuilder.must(searchShould);
            }
        }

    }

}
