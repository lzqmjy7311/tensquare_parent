package com.tensquare.article.dao;

import com.tensquare.article.po.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
  * 评论Dao
  * @author Administrator
  *
  */
public interface CommentRepository extends MongoRepository<Comment,String> {

    /**
     *    
     *  * 根据文章ID查询评论列表    
     *  * @param articleid    
     *  * @return    
     *  
     */
    public List<Comment> findByArticleid(String articleid);
}
