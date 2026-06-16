package com.os10.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class ScienceDataDO implements Serializable {
    private static final long serialVersionUID = 5251791855095670670L;

    private String uuid;

    @JSONField(name = "is_display")
    private String isDisplay;

    @JSONField(name = "src-type")
    private String srcType;

    @JSONField(name = "src-data")
    private String srcData;

    private String status;

    private String version;

    @JSONField(name = "topic_list")
    private List<Map<String, Object>> topicList;

    @JSONField(name = "topic_black_list")
    private String topicBlackList;

    // record fields
    private String title;

    @JSONField(name = "title-en")
    private String titleEn;

    @JSONField(name = "abstract")
    private String abstracts;

    @JSONField(name = "abstract-en")
    private String abstractEn;

    private List<String> keyword;

    @JSONField(name = "keyword-en")
    private List<String> keywordEn;

    private String author;

    @JSONField(name = "author-en")
    private String authorEn;

    private String doi;

    private String cstr;

    @JSONField(name = "pub-date")
    private String pubDate;

    @JSONField(name = "pub-year")
    private String pubYear;

    @JSONField(name = "dataset-id")
    private String datasetId;

    @JSONField(name = "dataset-type")
    private String datasetType;

    private String type;

    private String lang;

    private String size;

    @JSONField(name = "file-count")
    private String fileCount;

    @JSONField(name = "record-count")
    private String recordCount;

    @JSONField(name = "start-date")
    private String startDate;

    @JSONField(name = "end-date")
    private String endDate;

    @JSONField(name = "spatial-dimension")
    private String spatialDimension;

    private String facility;

    @JSONField(name = "group-name")
    private String groupName;

    @JSONField(name = "provider-name")
    private String providerName;

    private String correspondent;

    private String uri;

    private String url;

    @JSONField(name = "image-url")
    private String imageUrl;

    @JSONField(name = "coverUrl")
    private String coverUrl;

    private String rights;

    private String share;

    private String reference;

    @JSONField(name = "source-db")
    private String sourceDb;

    private String pid;

    private String issue;

    private String version_field;

    @JSONField(name = "browse_count")
    private Integer browseCount;

    @JSONField(name = "download_count")
    private Integer downloadCount;

    // nested objects
    private List<Map<String, Object>> classification;

    private List<Map<String, Object>> contributor;

    private List<Map<String, Object>> funding;

    private List<Map<String, Object>> project;

    private Map<String, Object> publisher;

    private Map<String, Object> copyRight;

    private Map<String, Object> article;

    // process fields
    @JSONField(name = "insert-date")
    private String insertDate;

    @JSONField(name = "insert-time")
    private String insertTime;

    @JSONField(name = "insert-year")
    private String insertYear;

    @JSONField(name = "update-date")
    private String updateDate;

    @JSONField(name = "update-time")
    private String updateTime;

    @JSONField(name = "update-year")
    private String updateYear;

    private String operator;

    private String source;

    // extend fields
    @JSONField(name = "article-uuid")
    private String articleUuid;

    @JSONField(name = "book-uuid")
    private String bookUuid;

    @JSONField(name = "patent-uuid")
    private String patentUuid;

    @JSONField(name = "project-uuid")
    private String projectUuid;

    @JSONField(name = "report-uuid")
    private String reportUuid;

    @JSONField(name = "data-uuid")
    private String dataUuid;

    @JSONField(name = "data-type")
    private String dataType;

    @JSONField(name = "file-type")
    private String fileType;

    @JSONField(name = "enrichment-article-uuid")
    private String enrichmentArticleUuid;

    @JSONField(name = "authority-scope")
    private String authorityScope;

    @JSONField(name = "blockchain_id")
    private String blockchainId;

    private String institution;

    @JSONField(name = "institution-en")
    private String institutionEn;

    private List<Map<String, Object>> contacts;

    private List<Map<String, Object>> supplement;
}
