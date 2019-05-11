package com.tensquare.qa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.qa.po.Problem;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 *
 * @author Administrator
 */
public interface ProblemRepository extends JpaRepository<Problem, String>, JpaSpecificationExecutor<Problem> {

    /**
     * 根据标签ID查询最新回答的问题分页列表
     *
     * @param labelid
     * @param pageable
     * @return
     */
    @Query("select p from Problem p where p.id in (select problemid from Pl where labelid =?1) order by p.replytime desc")
    Page<Problem> findByLabelidOrderByReplytimeDesc(String labelid, Pageable pageable);

    /**
     * 根据标签ID查询热门问题分页列表
     *
     * @param labelid
     * @param pageable
     * @return
     */
    @Query("select p from Problem p where p.id in (select problemid from Pl where labelid = ?1) order by p.reply desc")
    Page<Problem> findByLabelidOrderByReplyDesc(String labelid, Pageable pageable);

    /**
     * 根据标签ID查询回复是0的问题分页列表，且按照创建时间降序排序
     *
     * @param labelid
     * @param pageable
     * @return
     */
    @Query("select p from Problem p where p.id in (select problemid from Pl where labelid = ?1) and p.reply = 0 order by p.createtime desc")
    Page<Problem> findByLabelidNoReplyOrderByCreatetimeDesc(String labelid, Pageable pageable);
}
