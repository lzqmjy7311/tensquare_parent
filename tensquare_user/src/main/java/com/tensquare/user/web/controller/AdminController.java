package com.tensquare.user.web.controller;

import com.itcast.common.constants.StatusCode;
import com.itcast.common.dto.PageResultDTO;
import com.itcast.common.dto.ResultDTO;
import com.itcast.common.utils.JwtUtil;
import com.tensquare.user.po.Admin;
import com.tensquare.user.service.AdminService;
import com.tensquare.user.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制器层
 *
 * @author BoBoLaoShi
 */
@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private HttpServletRequest request;

    /**
     * 增加
     *
     * @param admin
     */
    @PostMapping
    public ResultDTO add(@RequestBody Admin admin) {
        adminService.saveAdmin(admin);
        return new ResultDTO(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param admin
     */
    @PutMapping("/{id}")
    public ResultDTO edit(@RequestBody Admin admin, @PathVariable String id) {
        admin.setId(id);
        adminService.updateAdmin(admin);
        return new ResultDTO(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @DeleteMapping("/{id}")
    public ResultDTO remove(@PathVariable String id) {
        //1.先鉴权，必须是管理员角色才能删除用户
        //获取头信息，约定头信息key为Authorization
        String authorizationHeader = request.getHeader("JwtAuthorization");
        //判断是否为空
        if(null == authorizationHeader){
            return new ResultDTO(false,StatusCode.ACCESSERROR,"权限不足1");
        }
        //判断授权头信息中是否是以“Bearer ”开头
        if(!authorizationHeader.startsWith("Bearer ")){
            return new ResultDTO(false,StatusCode.ACCESSERROR,"权限不足2");
        }

        //获取载荷
        Claims claims =null;
        try {
            //获取令牌
            String token=authorizationHeader.substring(7);
            claims = jwtUtil.parseJWT(token);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultDTO(false,StatusCode.ACCESSERROR,"权限不足3");
        }

        //判断载荷是否为空
        if(null == claims){
            return new ResultDTO(false,StatusCode.ACCESSERROR,"权限不足4");
        }
        //判断令牌中的自定义载荷中的角色是否是admin
        if(!"admin".equals(claims.get("roles"))){
            return new ResultDTO(false,StatusCode.ACCESSERROR,"权限不足5");
        }

        //2.删除用户
        this.userService.deleteUserById(id);
        return new ResultDTO(true,StatusCode.OK,"删除成功");
    }

    /**
     * 查询全部数据
     *
     * @return
     */
    @GetMapping
    public ResultDTO list() {
        return new ResultDTO(true, StatusCode.OK, "查询成功", adminService.findAdminList());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @GetMapping("/{id}")
    public ResultDTO listById(@PathVariable String id) {
        final Admin adminById = adminService.findAdminById(id);
        return new ResultDTO(true, StatusCode.OK, "查询成功", adminById);
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @PostMapping("/search")
    public ResultDTO list(@RequestBody Map searchMap) {
        final List<Admin> adminList = adminService.findAdminList(searchMap);
        return new ResultDTO(true, StatusCode.OK, "查询成功", adminList);
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
        Page<Admin> pageResponse = adminService.findAdminListPage(searchMap, page, size);
        final PageResultDTO<Admin> adminPage = new PageResultDTO<>(pageResponse.getTotalElements(), pageResponse.getContent());
        return new ResultDTO(true, StatusCode.OK, "查询成功", adminPage);
    }

    /**
     * 管理员登录
     *
     * @param loginMap
     * @return
     */
    @PostMapping("/login")
    public ResultDTO login(@RequestBody Map<String, String> loginMap) {

        final Admin admin = this.adminService.findAdminByLoginnameAndPassword(loginMap.get("loginname"), loginMap.get("password"));

        if (admin != null) {
            final String userToken = jwtUtil.createJWT(admin.getId(), admin.getLoginname(), "admin");
            final Map<String, String> map = new HashMap<>();
            map.put("token", userToken);
            //用来给前端直接显示用户名用的，如果前端需要其他的东东，则也可以继续添加
            map.put("name", admin.getLoginname());
            return new ResultDTO(true, StatusCode.OK, "登录成功", map);
        } else {
            return new ResultDTO(false, StatusCode.LOGINERROR, "账号或密码错误");
        }

    }

}
