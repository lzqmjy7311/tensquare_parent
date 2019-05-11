package com.itcast.base.dao;

import com.itcast.base.po.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 标签数据访问接口
 * JpaRepository<实体类类型,主键类型>：用来完成基本的CRUD操作
 * JpaSpecificationExecutor<实体类类型>：用于复杂查询（分页等查询操作）
 */
public interface LabelRepository extends JpaRepository<Label, String>, JpaSpecificationExecutor<Label> {

}
