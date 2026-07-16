package com.os10.controller;

import com.os10.entity.ScienceDataDO;
import com.os10.dto.ScienceDataQuery;
import com.os10.service.ScienceDataServiceImpl;
import com.os10.util.BaseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 科学数据管理 REST API
 * 提供科学数据的增、删、查、改及聚合分析接口
 */
@RestController
@RequestMapping("/sciencedata")
public class ScienceDataController {

    @Autowired
    private ScienceDataServiceImpl scienceDataService;

    /**
     * 创建/编辑科学数据
     * - 未传 uuid 视为新增：基于 title+pubDate+doi 生成 MD5 作为 id，重复则报错
     * - 已传 uuid 视为更新：根据 uuid 更新整条文档
     * 写入时自动将扁平 DTO 转换为 ES 嵌套结构 (record / extend / process)
     *
     * @param scienceDataDO 科学数据实体（必填字段：title, pubDate）
     * @return { code, msg, data }
     */
    @PostMapping("/v1/edit")
    public BaseMessage editScienceData(@RequestBody ScienceDataDO scienceDataDO){
        return scienceDataService.editScienceData(scienceDataDO);
    }

    /**
     * 科学数据列表分页查询
     * 支持关键词搜索、分面过滤、排序、分页；查询结果从嵌套结构展平为扁平格式返回
     *
     * @param scienceDataQuery 查询参数
     * @return { code, msg, data: { content: [...], total: long } }
     */
    @PostMapping("/v1/list")
    public BaseMessage listScienceData(@RequestBody ScienceDataQuery scienceDataQuery){
        return scienceDataService.listScienceData(scienceDataQuery);
    }

    /**
     * 科学数据聚合分析
     * 默认聚合字段：record.classification.first/second（分类）、record.pub-year（年份）、
     * record.dataset-type（数据集类型）、record.type（资源类型）
     *
     * @param scienceDataQuery 查询参数（支持 aggregations 自定义聚合字段）
     * @return { code, msg, data: { 聚合字段名: { buckets: [...] } } }
     */
    @PostMapping("/v1/aggregations")
    public BaseMessage listScienceDataAggregations(@RequestBody ScienceDataQuery scienceDataQuery){
        return scienceDataService.listScienceDataAggregations(scienceDataQuery);
    }

    /**
     * 获取最新科学数据（首页展示用）
     * GET /sciencedata/v1/latest?size=3
     * 按 record.pub-date 降序，返回展平后的数据
     */
    @GetMapping("/v1/latest")
    public BaseMessage getLatestScienceData(@RequestParam(defaultValue = "3") int size) {
        return scienceDataService.listLatestScienceData(size);
    }

    /**
     * 根据 uuid 获取单条科学数据详情
     * 返回结果从嵌套结构展平为扁平格式
     *
     * @param id 科学数据 uuid
     * @return { code, msg, data: ScienceDataDO }
     */
    @GetMapping("/v1/getOne/{id}")
    public BaseMessage getScienceData(@PathVariable(name = "id") String id){
        return scienceDataService.getScienceData(id);
    }

}
