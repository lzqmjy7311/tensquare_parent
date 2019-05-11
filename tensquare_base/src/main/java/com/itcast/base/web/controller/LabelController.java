package com.itcast.base.web.controller;

import com.itcast.base.po.Label;
import com.itcast.base.service.LabelService;
import com.itcast.common.constants.StatusCode;
import com.itcast.common.dto.PageResultDTO;
import com.itcast.common.dto.ResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 标签控制层
 */
@RestController
@RequestMapping("/label")
@CrossOrigin // 允许跨域请求
public class LabelController {
    @Autowired
    private LabelService labelService;

    /**
     * 添加一个
     * @param label
     * @return
     */
    @PostMapping
    public ResultDTO add(@RequestBody Label label){
        labelService.saveLabel(label);
        return new ResultDTO(true, StatusCode.OK,"添加成功");
    }

    /**
     * 修改编辑
     * @param label
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public ResultDTO edit(@RequestBody Label label,@PathVariable String id){
        label.setId(id);
        labelService.updateLabel(label);
        return new ResultDTO(true, StatusCode.OK,"修改成功");
    }

    /**
     * 根据id删除一个
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResultDTO remove(@PathVariable String id){
        labelService.deleteLabelById(id);
        return new ResultDTO(true, StatusCode.OK,"删除成功");
    }
    
    /**
     * 查询所有
     * @return
     */
    @GetMapping
    public ResultDTO list(){
        List<Label> list = labelService.findLabelList();
        return new ResultDTO(true, StatusCode.OK,"查询成功",list);
    }

    /**
     * 根据id查询一个
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResultDTO listById(@PathVariable String id){
        Label label = labelService.findLabelById(id);
        return new ResultDTO(true, StatusCode.OK,"查询成功",label);
    }

    /**
     * 条件查询
     * @return
     */
    @PostMapping("/search")
    public ResultDTO list(@RequestBody Map<String,Object> searchMap){
        List<Label> list = labelService.findLabelList(searchMap);
        return new ResultDTO(true, StatusCode.OK,"查询成功",list);
    }

    /**
     * 组合条件分页查询列表
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    public ResultDTO list(@RequestBody Map<String,Object> searchMap,@PathVariable int page,@PathVariable int size){
        Page<Label> pageResponse = labelService.findLabelListPage(searchMap, page, size);
        PageResultDTO<Label> pageResultDTO=new PageResultDTO<>(pageResponse.getTotalElements(),pageResponse.getContent());
        return new ResultDTO(true, StatusCode.OK,"查询成功",pageResultDTO);
    }

}