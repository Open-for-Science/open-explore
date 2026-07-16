package com.os10.controller;

import com.os10.dto.FacilityQuery;
import com.os10.entity.FacilityDO;
import com.os10.service.FacilityServiceImpl;
import com.os10.util.BaseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 大型科学装置管理控制器
 */
@RestController
@RequestMapping("/facility")
public class FacilityController {

    @Autowired
    private FacilityServiceImpl facilityService;

    /**
     * 查询装置列表（分页、搜索、排序、分面）
     * POST /facility/v1/list
     */
    @PostMapping("/v1/list")
    public BaseMessage listFacility(@RequestBody FacilityQuery facilityQuery) {
        return facilityService.listFacility(facilityQuery);
    }

    /**
     * 装置分面聚合
     * POST /facility/v1/aggregations
     */
    @PostMapping("/v1/aggregations")
    public BaseMessage listFacilityAggregations(@RequestBody FacilityQuery facilityQuery) {
        return facilityService.listFacilityAggregations(facilityQuery);
    }

    /**
     * 根据 uuid 获取单条装置详情
     * GET /facility/v1/getOne/{id}
     */
    @GetMapping("/v1/getOne/{id}")
    public BaseMessage getFacility(@PathVariable(name = "id") String id) {
        return facilityService.getFacility(id);
    }

    /**
     * 新增/编辑装置
     * POST /facility/v1/edit
     */
    @PostMapping("/v1/edit")
    public BaseMessage editFacility(@RequestBody FacilityDO facilityDO) {
        return facilityService.editFacility(facilityDO);
    }

    /**
     * 获取最新装置（首页展示用）
     * GET /facility/v1/latest?size=3
     */
    @GetMapping("/v1/latest")
    public BaseMessage getLatestFacility(@RequestParam(defaultValue = "3") int size) {
        return facilityService.listLatestFacility(size);
    }
}
