package com.ljq.controller;

import com.ljq.annotation.SystemLog;
import com.ljq.domain.ResponseResult;
import com.ljq.domain.entity.User;
import com.ljq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/userInfo")
    @SystemLog(bussinessName = "展示用户个人信息")
    public ResponseResult userInfo(){
        return userService.userInfo();
    }

    @PutMapping("/userInfo")
    public ResponseResult updateUser(@RequestBody User user){
        return userService.updateUser(user);
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }
}
