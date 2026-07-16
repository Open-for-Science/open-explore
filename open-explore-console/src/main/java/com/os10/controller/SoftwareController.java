package com.os10.controller;

import com.os10.dto.SoftwareQuery;
import com.os10.entity.SoftwareDO;
import com.os10.service.SoftwareServiceImpl;
import com.os10.util.BaseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 科学软件管理控制器
 */
@RestController
@RequestMapping("/software")
public class SoftwareController {

    @Autowired
    private SoftwareServiceImpl softwareService;

    /**
     * 查询软件列表（分页、搜索、排序、分面）
     * POST /software/v1/list
     */
    @PostMapping("/v1/list")
    public BaseMessage listSoftware(@RequestBody SoftwareQuery softwareQuery) {
        return softwareService.listSoftware(softwareQuery);
    }

    /**
     * 软件分面聚合
     * POST /software/v1/aggregations
     */
    @PostMapping("/v1/aggregations")
    public BaseMessage listSoftwareAggregations(@RequestBody SoftwareQuery softwareQuery) {
        return softwareService.listSoftwareAggregations(softwareQuery);
    }

    /**
     * 根据 uuid 获取单条软件详情
     * GET /software/v1/getOne/{id}
     */
    @GetMapping("/v1/getOne/{id}")
    public BaseMessage getSoftware(@PathVariable(name = "id") String id) {
        return softwareService.getSoftware(id);
    }

    /**
     * 新增/编辑软件
     * POST /software/v1/edit
     */
    @PostMapping("/v1/edit")
    public BaseMessage editSoftware(@RequestBody SoftwareDO softwareDO) {
        return softwareService.editSoftware(softwareDO);
    }

    /**
     * 获取最新软件（首页展示用）
     * GET /software/v1/latest?size=3
     */
    @GetMapping("/v1/latest")
    public BaseMessage getLatestSoftware(@RequestParam(defaultValue = "3") int size) {
        return softwareService.listLatestSoftware(size);
    }
}
