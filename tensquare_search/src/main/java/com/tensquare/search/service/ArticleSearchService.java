package com.tensquare.search.service;

import com.itcast.common.utils.IdWorker;
import com.tensquare.search.dao.ArticleEsRepository;
import com.tensquare.search.po.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文章搜索的业务层
 */
@Service
public class ArticleSearchService {

    @Autowired
    private ArticleEsRepository articleEsRepository;
    @Autowired
    private IdWorker idWorker;

    /**
     * 保存文章到索引库中
     *
     * @param article
     */
    public void saveArticle(Article article) {
        article.setId(idWorker.nextId() + "");
        this.articleEsRepository.save(article);
    }
}
