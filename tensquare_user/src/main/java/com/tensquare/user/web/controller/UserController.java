package com.tensquare.user.web.controller;

import com.itcast.common.constants.StatusCode;
import com.itcast.common.dto.PageResultDTO;
import com.itcast.common.dto.ResultDTO;
import com.itcast.common.utils.JwtUtil;
import com.tensquare.user.po.User;
import com.tensquare.user.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 控制器层
 *
 * @author BoBoLaoShi
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 增加
     *
     * @param user
     */
    @PostMapping
    public ResultDTO add(@RequestBody User user) {
        userService.saveUser(user);
        return new ResultDTO(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param user
     */
    @PutMapping("/{id}")
    public ResultDTO edit(@RequestBody User user, @PathVariable String id) {
        user.setId(id);
        userService.updateUser(user);
        return new ResultDTO(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @DeleteMapping("/{id}")
    public ResultDTO remove(@PathVariable String id) {
        final Claims claims = (Claims) request.getAttribute("admin_claims");
        if (claims == null) {
            return new ResultDTO(false, StatusCode.ACCESSERROR, "权限不足");
        }
        userService.deleteUserById(id);
        return new ResultDTO(true, StatusCode.OK, "删除成功");
    }

    /**
     * 查询全部数据
     *
     * @return
     */
    @GetMapping
    public ResultDTO list() {
        return new ResultDTO(true, StatusCode.OK, "查询成功", userService.findUserList());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @GetMapping("/{id}")
    public ResultDTO listById(@PathVariable String id) {
        return new ResultDTO(true, StatusCode.OK, "查询成功", userService.findUserById(id));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @PostMapping("/search")
    public ResultDTO list(@RequestBody Map searchMap) {
        return new ResultDTO(true, StatusCode.OK, "查询成功", userService.findUserList(searchMap));
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
        Page<User> pageResponse = userService.findUserListPage(searchMap, page, size);
        return new ResultDTO(true, StatusCode.OK, "查询成功", new PageResultDTO<User>(pageResponse.getTotalElements(), pageResponse.getContent()));
    }

    /**
     * 发送短信验证码
     *
     * @param mobile
     * @return
     */
    @PostMapping("/sendsms/{mobile}")
    public ResultDTO sendSmsCheckcode(@PathVariable String mobile) {
        userService.saveSmsCheckcode(mobile);
        return new ResultDTO(true, StatusCode.OK, "发送成功");
    }

    /**
     * 用户注册
     *
     * @param user
     */
    @PostMapping("/register/{checkcode}")
    public ResultDTO register(@RequestBody User user, @PathVariable String checkcode) {
        userService.saveUser(user, checkcode);
        return new ResultDTO(true, StatusCode.OK, "用户注册成功");
    }

    /**
     * 用户登录
     *
     * @param loginMap
     * @return
     */
    @PostMapping("/login")
    public ResultDTO login(@RequestBody Map<String, String> loginMap) {
        //调用业务层查询
        User user = userService.findUserByMobileAndPassword(loginMap.get("mobile"), loginMap.get("password"));
        //判断
        if (null != user) {
            //登录成功
            //签发token
            String token = jwtUtil.createJWT(user.getId(), user.getNickname(), "user");
            //构建Map，用来封装token（最终转json）
            Map map = new HashMap();
            map.put("token", token);
            //用户昵称
            map.put("name", user.getNickname());
            //用户头像
            map.put("avatar", user.getAvatar());
            return new ResultDTO(true, StatusCode.OK, "登录成功", map);
        } else {
            //登录失败
            return new ResultDTO(false, StatusCode.LOGINERROR, "用户名或密码错误");
        }
    }

    /**
     * 增加粉丝数(给其他微服务调用)
     *
     * @param userid
     * @param x
     */
    @PostMapping("/incfans/{userid}/{x}")
    public void incFanscount(@PathVariable String userid, @PathVariable int x) {
        userService.updateFanscountIncByUserid(userid, x);
    }

    /**
     * 增加关注数
     *
     * @param userid
     * @param x
     */
    @PostMapping("/incfollow/{userid}/{x}")
    public void incFollowcount(@PathVariable String userid, @PathVariable int x) {
        userService.updateFollowcountIncByUserid(userid, x);
    }
}
