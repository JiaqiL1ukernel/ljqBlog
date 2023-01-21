package com.ljq.controller;

import com.ljq.domain.ResponseResult;
import com.ljq.domain.entity.LoginUser;
import com.ljq.domain.entity.User;
import com.ljq.domain.vo.AdminUserInfoVo;
import com.ljq.domain.vo.MenuVo;
import com.ljq.domain.vo.RoutersVo;
import com.ljq.domain.vo.UserInfoVo;
import com.ljq.service.LoginService;
import com.ljq.service.MenuService;
import com.ljq.service.RoleService;
import com.ljq.service.UserService;
import com.ljq.utils.BeanCopyUtil;
import com.ljq.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        return loginService.login(user);
    }

    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        //获取用户id
        Long userId = SecurityUtils.getUserId();
        //根据userId查询权限信息
        List<String> perms = menuService.selectPermsByUserId(userId);
        //根据userId查询角色信息
        List<String> roles = roleService.selectRoleKeyByUserId(userId);
        //封装成ResponseResult返回
        LoginUser loginUser = SecurityUtils.getLoginUser();
        UserInfoVo userInfoVo = BeanCopyUtil.copyBean(loginUser.getUser(), UserInfoVo.class);
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, roles, userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        //获取userId
        Long userId = SecurityUtils.getUserId();
        //查询
        List<MenuVo> menus = menuService.selectRouterMenuTreeByUserId(userId);

        return ResponseResult.okResult(new RoutersVo(menus));
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }


}
