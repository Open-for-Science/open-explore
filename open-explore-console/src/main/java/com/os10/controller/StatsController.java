package com.os10.controller;

import com.alibaba.fastjson.JSONObject;
import com.os10.service.ArticleServiceImpl;
import com.os10.service.ScienceDataServiceImpl;
import com.os10.service.SoftwareServiceImpl;
import com.os10.service.FacilityServiceImpl;
import com.os10.util.BaseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页统计信息 API
 * 一次性返回各索引的总量
 */
@RestController
@RequestMapping("/stats")
public class StatsController {

    @Autowired
    private ArticleServiceImpl articleService;

    @Autowired
    private ScienceDataServiceImpl scienceDataService;

    @Autowired
    private SoftwareServiceImpl softwareService;

    @Autowired
    private FacilityServiceImpl facilityService;

    /**
     * 获取全站统计总量
     * GET /stats/v1/totals
     *
     * @return { code, msg, data: { articleTotal, scienceDataTotal, softwareTotal, facilityTotal, xxTotal1, xxTotal2 } }
     */
    @GetMapping("/v1/totals")
    public BaseMessage getTotals() {
        JSONObject data = new JSONObject();
        data.put("articleTotal", articleService.countTotal()+"+");
        data.put("scienceDataTotal", scienceDataService.countTotal()+"+");
        data.put("softwareTotal", softwareService.countTotal()+"+");
        data.put("facilityTotal", facilityService.countTotal()+"+");
        // xxTotal1/xxTotal2 保持兼容旧前端，但实际值复用 softwareTotal/facilityTotal
        return BaseMessage.success(data);
    }
}
