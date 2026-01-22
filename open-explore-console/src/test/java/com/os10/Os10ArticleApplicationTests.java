package com.os10;

import com.alibaba.fastjson.JSONObject;
import com.os10.dto.ArticleQuery;
import com.os10.service.ArticleServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = Os10ArticleApplication.class)
@RunWith(SpringRunner.class)
public class Os10ArticleApplicationTests {

    @Autowired
    private ArticleServiceImpl articleService;


    @Test
    public void contextLoads() {

//        for (int i = 1; i < 11; i++) {
//
//
//            ArticleDO articleDTO = new ArticleDO();
//
//            articleDTO.setTitle("我是无敌的标题"+i);
//            articleDTO.setAbstracts("我是无敌的摘要"+i);
//            articleDTO.setJournal("期刊"+i);
//            articleDTO.setPublisher("出版商"+i);
//            articleDTO.setDate("2025-10-27");
//            articleDTO.setKeyword(Collections.singletonList("关键词"+i));
//            articleDTO.setSubject(Collections.singletonList("学科"+i));
//            articleDTO.setAuthor(Collections.singletonList("作者"+i));
//            articleDTO.setInstitution(Collections.singletonList("机构"+i));
//            articleDTO.setCollection(Collections.singletonList("收录"+i));
//
//            articleDTO.setType("期刊论文");
//
//            articleDTO.setIssn("1111-2222");
//            articleDTO.setIssue("01");
//            articleDTO.setVolume("02");
//            articleDTO.setFirstPage("1");
//            articleDTO.setLink("20");
//            articleDTO.setLink("wwww.baidu.com");
//            articleDTO.setDoi("doi/111111111");
//            articleDTO.setLicense("CC BY");
//            articleDTO.setFunding("资助基金项目"+i);
////        articleDTO.setUuid("55d8c21d11bd7419c2b5ad9ab32fad1a");
//
//            System.err.println(JSONObject.toJSONString(articleService.editArticle(articleDTO)));
//        }

        ArticleQuery articleQuery = new ArticleQuery();
        articleQuery.setPage("2");

//        System.err.println(JSONObject.toJSONString(articleService.listArticles(articleQuery)));
        System.err.println(JSONObject.toJSONString(articleService.listArticleAggregations(articleQuery)));
    }


}
