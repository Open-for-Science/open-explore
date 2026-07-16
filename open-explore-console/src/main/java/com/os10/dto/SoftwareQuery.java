package com.os10.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoftwareQuery implements Serializable {

    private static final long serialVersionUID = 2953323413311637798L;

    private String query;

    private String strategy;

    @JSONField(name = "order_field")
    private String orderField;

    @JSONField(name = "order_direction")
    private String orderDirection;

    private String page;

    private String size;

    private Map<String, String> aggregations;

    private Map<String, Object> extendParams;
}
