package com.os10.common.support;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EsScienceDataService extends EsBaseService{

    private String esIp;
    private int esPort;
    private String scienceDataIndexName;
    private String scienceDataIndexType;

    // 使用构造器注入
    public EsScienceDataService(
            @Value("${es.sciencedata.ip:${es.ip}}") String esIp,
            @Value("${es.sciencedata.port:${es.port}}") int esPort,
            @Value("${es.sciencedata.index_name}") String scienceDataIndexName,
            @Value("${es.sciencedata.index_type}") String scienceDataIndexType) {

        this.esIp = esIp;
        this.esPort = esPort;
        this.scienceDataIndexName = scienceDataIndexName;
        this.scienceDataIndexType = scienceDataIndexType;

        System.err.println(esIp + "--" + esPort + "--" + scienceDataIndexName + "--" + scienceDataIndexType);
        this.setIp(esIp);
        this.setPort(esPort);
        this.setIndex(scienceDataIndexName);
        this.setType(scienceDataIndexType);
    }

    public void setEsIp(String esIp) {
        this.esIp = esIp;
    }

    public void setEsPort(int esPort) {
        this.esPort = esPort;
    }

    public void setScienceDataIndexName(String scienceDataIndexName) {
        this.scienceDataIndexName = scienceDataIndexName;
    }

    public void setScienceDataIndexType(String scienceDataIndexType) {
        this.scienceDataIndexType = scienceDataIndexType;
    }

    public String getEsIp() {
        return esIp;
    }

    public int getEsPort() {
        return esPort;
    }

    public String getScienceDataIndexName() {
        return scienceDataIndexName;
    }

    public String getScienceDataIndexType() {
        return scienceDataIndexType;
    }
}
