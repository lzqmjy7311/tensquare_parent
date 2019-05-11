package com.tensquare.search.web.controller;

import com.itcast.common.constants.StatusCode;
import com.itcast.common.dto.ResultDTO;
import com.tensquare.search.po.Article;
import com.tensquare.search.service.ArticleSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleSearchController {

    @Autowired
    private ArticleSearchService articleSearchService;

    /**
     * 保存文章到es中
     *
     * @param article
     * @return
     */
    @PostMapping
    public ResultDTO save(@RequestBody Article article) {

        this.articleSearchService.saveArticle(article);
        return new ResultDTO(true, StatusCode.OK, "保存成功");

    }
}

