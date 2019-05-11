package com.tensquare.article.web.controller;


import com.itcast.common.constants.StatusCode;
import com.itcast.common.dto.ResultDTO;
import com.tensquare.article.po.Comment;
import com.tensquare.article.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 新增评论
     * @param comment
     * @return
     */
    @PostMapping
    public ResultDTO save(@RequestBody Comment comment) {
        commentService.add(comment);
        return new ResultDTO(true, StatusCode.OK, "提交成功");
    }

    /**
     * 根据文章ID查询评论列表
     * @param articleid
     * @return
     */
    @GetMapping("/article/{articleid}")
    public ResultDTO findByArticleid(@PathVariable String articleid) {
        final List<Comment> byArticleid = this.commentService.findByArticleid(articleid);
        return new ResultDTO(true, StatusCode.OK, "查询成功", byArticleid);
    }
}
