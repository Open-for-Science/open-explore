package com.os10.controller;

import com.os10.entity.ArticleDO;
import com.os10.dto.ArticleQuery;
import com.os10.service.ArticleServiceImpl;
import com.os10.util.BaseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文章管理 REST API
 * 提供文章的增、删、查、改及聚合分析接口
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleServiceImpl articleService;

    /**
     * 创建/编辑文章
     * - 未传 uuid 视为新增：基于 title+date+journal 生成 MD5 作为 id，重复则报错
     * - 已传 uuid 视为更新：根据 uuid 覆盖式更新整条文档
     *
     * @param articleDO 文章实体（必填字段：title, date, journal）
     * @return { code, msg, data }
     */
    @PostMapping("/v1/edit")
    public BaseMessage editArticle(@RequestBody ArticleDO articleDO){
        return articleService.editArticle(articleDO);
    }

    /**
     * 文章列表分页查询
     * 支持关键词搜索、分面过滤、排序、分页
     *
     * @param articleQuery 查询参数（query全文搜索 / strategy精确策略 / aggregations分面 / page / size / order）
     * @return { code, msg, data: { content: [...], total: long } }
     */
    @PostMapping("/v1/list")
    public BaseMessage listArticles(@RequestBody ArticleQuery articleQuery){
        return articleService.listArticles(articleQuery);
    }

    /**
     * 文章聚合分析
     * 默认聚合字段：subject（学科）、journal（期刊）、year（年份）、collection、type
     * 可通过 aggregations 参数自定义聚合字段
     *
     * @param articleQuery 查询参数（支持 aggregations 自定义聚合字段 + 分面过滤）
     * @return { code, msg, data: { 聚合字段名: { buckets: [...] } } }
     */
    @PostMapping("/v1/aggregations")
    public BaseMessage listArticleAggregations(@RequestBody ArticleQuery articleQuery){
        return articleService.listArticleAggregations(articleQuery);
    }

    /**
     * 根据 uuid 获取单篇文章详情
     *
     * @param id 文章 uuid
     * @return { code, msg, data: ArticleDO }
     */
    @GetMapping("/v1/getOne/{id}")
    public BaseMessage editArticle(@PathVariable(name = "id") String id){
        return articleService.getArticle(id);
    }

    /**
     * 获取最新文章（首页展示用）
     * GET /article/v1/latest?size=3
     */
    @GetMapping("/v1/latest")
    public BaseMessage getLatestArticles(@RequestParam(defaultValue = "3") int size) {
        return articleService.listLatestArticles(size);
    }

}
