package com.tensquare.search.dao;

import com.tensquare.search.po.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 文章的es搜索的持久层
 */
public interface ArticleEsRepository extends ElasticsearchRepository<Article,String> {
}