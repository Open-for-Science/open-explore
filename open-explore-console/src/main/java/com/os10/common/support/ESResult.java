package com.os10.common.support;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ESResult {
    private JSONArray hits;
    private JSONObject aggs;
    private long total;
    private String scrollId;
    private Object otherData;
}
