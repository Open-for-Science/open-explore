package com.os10.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ArticleDO implements Serializable {
    private static final long serialVersionUID = 5251791855095670669L;

    private String uuid;

    private String title;

    private String abstracts;

    private List<String> keyword;

    private List<String> subject;

    private List<String> author;

    private List<String> institution;

    private String journal;

    private String publisher;

    private String date;

    private String year;

    private String link;

    private String issue;

    private String volume;

    @JSONField(name = "start_page")
    private String firstPage;

    @JSONField(name = "last_page")
    private String lastPage;

    private String doi;

    private List<String> collection;

    private String type;

    private String license;

    private String funding;

    private String issn;

}
