package com.itcast.base.service;

import com.itcast.base.dao.LabelRepository;
import com.itcast.base.po.Label;
import com.itcast.common.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * \标签业务逻辑类
 */
@Service
public class LabelService {

    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private IdWorker idWorker;

    /**
     * 保存一个标签
     *
     * @param label
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveLabel(Label label) {
        //设置ID
        label.setId(idWorker.nextId() + "");
        labelRepository.save(label);
    }

    /**
     * 更新一个标签
     *
     * @param label
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateLabel(Label label) {
        labelRepository.save(label);
    }

    /**
     * 删除一个标签
     *
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteLabelById(String id) {
        labelRepository.deleteById(id);
    }

    /**
     * 查询全部标签
     *
     * @return
     */
    public List<Label> findLabelList() {
        return labelRepository.findAll();
    }

    /**
     * 根据ID查询标签
     *
     * @return
     */
    public Label findLabelById(String id) {
        return labelRepository.findById(id).get();
    }

    /**
     * 根据复杂条件查询列表
     *
     * @param searchMap
     * @return
     */
    public List<Label> findLabelList(Map<String, Object> searchMap) {
        // 请求的业务条件对象
        Specification<Label> spec = getLabelSpecification(searchMap);
        return labelRepository.findAll(spec);
    }

    /**
     * 组合条件分页查询
     *
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public Page<Label> findLabelListPage(Map<String, Object> searchMap, int page, int size) {
        //请求的业务条件对象
        Specification<Label> spec = getLabelSpecification(searchMap);
        //2.请求的分页bean对象
        //参数1：当前页码的索引，zero-based page index 从0开始
        //参数2：每页显示的最大的记录数
        PageRequest pageRequest = PageRequest.of(page-1, size);
        return this.labelRepository.findAll(spec, pageRequest);
    }

    /**
     * 根据map封装出spec业务条件对象
     *
     * @param searchMap
     * @return
     */
    private Specification<Label> getLabelSpecification(Map<String, Object> searchMap) {

        Specification<Label> spec = (root, query, cb) -> {
            //临时存放条件结果的集合
            List<Predicate> predicateList = new ArrayList<>();

            //标签名字
            if (!StringUtils.isEmpty(searchMap.get("labelname"))) {
                predicateList.add(
                        cb.like(root.get("labelname").as(String.class), "%" + searchMap.get("labelname") + "%")
                );
            }
            //状态
            if (!StringUtils.isEmpty(searchMap.get("state"))) {
                predicateList.add(
                        cb.equal(root.get("state").as(String.class), searchMap.get("state"))
                );
            }
            //是否推荐
            if (!StringUtils.isEmpty(searchMap.get("recommend"))) {
                predicateList.add(
                        cb.equal(root.get("recommend").as(String.class), searchMap.get("recommend"))
                );
            }
            //使用and拼接条件
            return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
        };
        return spec;
    }

}















