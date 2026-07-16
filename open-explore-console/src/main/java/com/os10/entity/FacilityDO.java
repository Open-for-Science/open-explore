package com.os10.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FacilityDO implements Serializable {
    private static final long serialVersionUID = 5251791855095670671L;

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

    private String doi;

    private String cstr;

    @JSONField(name = "pub-date")
    private String pubDate;

    @JSONField(name = "pub-year")
    private String pubYear;

    @JSONField(name = "facility-name")
    private String facilityName;

    @JSONField(name = "facility-type")
    private String facilityType;

    @JSONField(name = "facility-location")
    private String facilityLocation;

    @JSONField(name = "facility-status")
    private String facilityStatus;

    @JSONField(name = "operation-year")
    private String operationYear;

    @JSONField(name = "image-url")
    private String imageUrl;

    @JSONField(name = "coverUrl")
    private String coverUrl;

    private String contributor;

    private String publisher;

    private String funding;

    private String rights;

    @JSONField(name = "is_display")
    private String isDisplay;

    private String status;
}
