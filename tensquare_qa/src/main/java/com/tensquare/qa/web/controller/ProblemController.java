package com.tensquare.qa.web.controller;

import com.itcast.common.constants.StatusCode;
import com.itcast.common.dto.PageResultDTO;
import com.itcast.common.dto.ResultDTO;
import com.tensquare.qa.client.LabelClient;
import com.tensquare.qa.po.Problem;
import com.tensquare.qa.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 控制器层
 *
 * @author BoBoLaoShi
 */
@RestController
@CrossOrigin
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    @Autowired
    private LabelClient labelClient;
    /**
     * 增加
     *
     * @param problem
     */
    @PostMapping
    public ResultDTO add(@RequestBody Problem problem) {
        problemService.saveProblem(problem);
        return new ResultDTO(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param problem
     */
    @PutMapping("/{id}")
    public ResultDTO edit(@RequestBody Problem problem, @PathVariable String id) {
        problem.setId(id);
        problemService.updateProblem(problem);
        return new ResultDTO(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @DeleteMapping("/{id}")
    public ResultDTO remove(@PathVariable String id) {
        problemService.deleteProblemById(id);
        return new ResultDTO(true, StatusCode.OK, "删除成功");
    }

    /**
     * 查询全部数据
     *
     * @return
     */
    @GetMapping
    public ResultDTO list() {
        return new ResultDTO(true, StatusCode.OK, "查询成功", problemService.findProblemList());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @GetMapping("/{id}")
    public ResultDTO listById(@PathVariable String id) {
        return new ResultDTO(true, StatusCode.OK, "查询成功", problemService.findProblemById(id));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @PostMapping("/search")
    public ResultDTO list(@RequestBody Map searchMap) {
        return new ResultDTO(true, StatusCode.OK, "查询成功", problemService.findProblemList(searchMap));
    }

    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @PostMapping("/search/{page}/{size}")
    public ResultDTO listPage(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<Problem> pageResponse = problemService.findProblemListPage(searchMap, page, size);
        return new ResultDTO(true, StatusCode.OK, "查询成功", new PageResultDTO<Problem>(pageResponse.getTotalElements(), pageResponse.getContent()));
    }

    /**
     * 根据标签ID和分页条件查询最新问题列表
     *
     * @param label
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/newlist/{label}/{page}/{size}")
    public ResultDTO listPageNewReplyByLabelid(@PathVariable String label, @PathVariable int page, @PathVariable int size) {
        Page<Problem> pageResponse = problemService.findProblemListPageNewReplyByLabelid(label, page, size);
        return new ResultDTO(true, StatusCode.OK, "查询成功",
                new PageResultDTO<Problem>(pageResponse.getTotalElements(), pageResponse.getContent()));
    }

    /**
     * 根据标签ID和分页条件查询热门回复的问题列表
     *
     * @param label
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/hotlist/{label}/{page}/{size}")
    public ResultDTO listPageHotReplyByLabelid(@PathVariable String label, @PathVariable int page, @PathVariable int size) {
        Page<Problem> pageResponse = problemService.findProblemListPageHotReplyByLabelid(label, page, size);
        return new ResultDTO(true, StatusCode.OK, "查询成功", new PageResultDTO<Problem>(pageResponse.getTotalElements(), pageResponse.getContent()));
    }

    /**
     * 根据标签ID查询没有回复的问题分页列表，且按照创建时间降序排序
     *
     * @param label
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/waitlist/{label}/{page}/{size}")
    public ResultDTO listPageWaitReplyByLabelid(@PathVariable String label, @PathVariable int page, @PathVariable int size) {
        Page<Problem> pageResponse = problemService.findProblemListPageNoReplyByLabelid(label, page, size);
        return new ResultDTO(true, StatusCode.OK, "查询成功", new PageResultDTO<Problem>(pageResponse.getTotalElements(), pageResponse.getContent()));
    }

    /**
     * 根据标签id，通过微服务来查询标签对象
     *
     * @param labelid
     * @return
     */
    @GetMapping("/label/{labelid}")
    public ResultDTO findLabelById(@PathVariable String labelid) {

        final ResultDTO resultDTO = labelClient.listById(labelid);
        return resultDTO;
    }

}
