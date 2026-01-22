package com.os10.controller;

import com.os10.entity.ArticleDO;
import com.os10.dto.ArticleQuery;
import com.os10.service.ArticleServiceImpl;
import com.os10.util.BaseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleServiceImpl articleService;

    @PostMapping("/v1/edit")
    public BaseMessage editArticle(@RequestBody ArticleDO articleDO){
        return articleService.editArticle(articleDO);
    }

    @PostMapping("/v1/list")
    public BaseMessage listArticles(@RequestBody ArticleQuery articleQuery){
        return articleService.listArticles(articleQuery);
    }

    @PostMapping("/v1/aggregations")
    public BaseMessage listArticleAggregations(@RequestBody ArticleQuery articleQuery){
        return articleService.listArticleAggregations(articleQuery);
    }

    @GetMapping("/v1/getOne/{id}")
    public BaseMessage editArticle(@PathVariable(name = "id") String id){
        return articleService.getArticle(id);
    }



}
