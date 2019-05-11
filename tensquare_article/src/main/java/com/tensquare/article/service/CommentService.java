package com.tensquare.article.service;

import com.itcast.common.utils.IdWorker;
import com.tensquare.article.dao.CommentRepository;
import com.tensquare.article.po.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Date;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private IdWorker idWorker;

    /**
     * 新增评论
     * @param comment
     */
    public void add(Comment comment) {
        comment.set_id(idWorker.nextId() + "");
        comment.setPublishdate(new Date());
        commentRepository.save(comment);
    }

    /**
     * 根据文章ID查询评论列表
     * @param articleid
     * @return
     */
    public List<Comment> findByArticleid(String articleid) {
        return this.commentRepository.findByArticleid(articleid);
    }


}
