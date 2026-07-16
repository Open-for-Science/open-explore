package com.os10.common.support;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EsFacilityService extends EsBaseService {

    private String esIp;
    private int esPort;
    private String facilityIndexName;
    private String facilityIndexType;

    public EsFacilityService(
            @Value("${es.facility.ip:${es.ip}}") String esIp,
            @Value("${es.facility.port:${es.port}}") int esPort,
            @Value("${es.facility.index_name}") String facilityIndexName,
            @Value("${es.facility.index_type}") String facilityIndexType) {

        this.esIp = esIp;
        this.esPort = esPort;
        this.facilityIndexName = facilityIndexName;
        this.facilityIndexType = facilityIndexType;

        System.err.println("EsFacilityService: " + esIp + "--" + esPort + "--" + facilityIndexName + "--" + facilityIndexType);
        this.setIp(esIp);
        this.setPort(esPort);
        this.setIndex(facilityIndexName);
        this.setType(facilityIndexType);
    }

    public void setEsIp(String esIp) { this.esIp = esIp; }
    public void setEsPort(int esPort) { this.esPort = esPort; }
    public void setFacilityIndexName(String facilityIndexName) { this.facilityIndexName = facilityIndexName; }
    public void setFacilityIndexType(String facilityIndexType) { this.facilityIndexType = facilityIndexType; }

    public String getEsIp() { return esIp; }
    public int getEsPort() { return esPort; }
    public String getFacilityIndexName() { return facilityIndexName; }
    public String getFacilityIndexType() { return facilityIndexType; }
}
