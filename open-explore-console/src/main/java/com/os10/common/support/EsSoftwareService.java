package com.os10.common.support;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EsSoftwareService extends EsBaseService {

    private String esIp;
    private int esPort;
    private String softwareIndexName;
    private String softwareIndexType;

    public EsSoftwareService(
            @Value("${es.software.ip:${es.ip}}") String esIp,
            @Value("${es.software.port:${es.port}}") int esPort,
            @Value("${es.software.index_name}") String softwareIndexName,
            @Value("${es.software.index_type}") String softwareIndexType) {

        this.esIp = esIp;
        this.esPort = esPort;
        this.softwareIndexName = softwareIndexName;
        this.softwareIndexType = softwareIndexType;

        System.err.println("EsSoftwareService: " + esIp + "--" + esPort + "--" + softwareIndexName + "--" + softwareIndexType);
        this.setIp(esIp);
        this.setPort(esPort);
        this.setIndex(softwareIndexName);
        this.setType(softwareIndexType);
    }

    public void setEsIp(String esIp) { this.esIp = esIp; }
    public void setEsPort(int esPort) { this.esPort = esPort; }
    public void setSoftwareIndexName(String softwareIndexName) { this.softwareIndexName = softwareIndexName; }
    public void setSoftwareIndexType(String softwareIndexType) { this.softwareIndexType = softwareIndexType; }

    public String getEsIp() { return esIp; }
    public int getEsPort() { return esPort; }
    public String getSoftwareIndexName() { return softwareIndexName; }
    public String getSoftwareIndexType() { return softwareIndexType; }
}
