package com.os10.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SoftwareDO implements Serializable {
    private static final long serialVersionUID = 5251791855095670670L;

    private String uuid;

    private String title;

    @JSONField(name = "title-en")
    private String titleEn;

    private String abstracts;

    @JSONField(name = "abstract-en")
    private String abstractEn;

    private List<String> keyword;

    @JSONField(name = "keyword-en")
    private List<String> keywordEn;

    private List<String> author;

    @JSONField(name = "author-en")
    private List<String> authorEn;

    private String doi;

    private String cstr;

    private String version;

    @JSONField(name = "pub-date")
    private String pubDate;

    @JSONField(name = "pub-year")
    private String pubYear;

    @JSONField(name = "software-name")
    private String softwareName;

    @JSONField(name = "software-version")
    private String softwareVersion;

    @JSONField(name = "programming-language")
    private List<String> programmingLanguage;

    private String platform;

    private String license;

    @JSONField(name = "source-url")
    private String sourceUrl;

    @JSONField(name = "documentation-url")
    private String documentationUrl;

    @JSONField(name = "download-url")
    private String downloadUrl;

    @JSONField(name = "image-url")
    private String imageUrl;

    private String contributor;

    private String publisher;

    private String funding;

    private String rights;

    @JSONField(name = "is_display")
    private String isDisplay;

    private String status;
}
