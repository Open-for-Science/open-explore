package com.os10.common.support;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EsArticleService extends EsBaseService{

    private String esIp;
    private int esPort;
    private String articleIndexName;
    private String articleIndexType;

    // 使用构造器注入
    public EsArticleService(
            @Value("${es.ip}") String esIp,
            @Value("${es.port}") int esPort,
            @Value("${es.article.index_name}") String articleIndexName,
            @Value("${es.article.index_type}") String articleIndexType) {

        this.esIp = esIp;
        this.esPort = esPort;
        this.articleIndexName = articleIndexName;
        this.articleIndexType = articleIndexType;

        System.err.println(esIp + "--" + esPort + "--" + articleIndexName + "--" + articleIndexType);
        this.setIp(esIp);
        this.setPort(esPort);
        this.setIndex(articleIndexName);
        this.setType(articleIndexType);
    }

    public void setEsIp(String esIp) {
        this.esIp = esIp;
    }

    public void setEsPort(int esPort) {
        this.esPort = esPort;
    }

    public void setArticleIndexName(String articleIndexName) {
        this.articleIndexName = articleIndexName;
    }

    public void setArticleIndexType(String articleIndexType) {
        this.articleIndexType = articleIndexType;
    }

    public String getEsIp() {
        return esIp;
    }

    public int getEsPort() {
        return esPort;
    }

    public String getArticleIndexName() {
        return articleIndexName;
    }

    public String getArticleIndexType() {
        return articleIndexType;
    }
}
