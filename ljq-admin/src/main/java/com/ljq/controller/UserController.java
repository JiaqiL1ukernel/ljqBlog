package com.ljq.controller;

import com.ljq.domain.ResponseResult;
import com.ljq.domain.dto.AddUserDto;
import com.ljq.domain.dto.UpdateUserDto;
import com.ljq.enums.AppHttpCodeEnum;
import com.ljq.exception.SystemException;
import com.ljq.service.UserService;
import com.ljq.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.RespectBinding;

@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum,Integer pageSize,String userName,String phonenumber,String status){
        return userService.listAllUsers(pageNum,pageSize,userName,phonenumber,status);
    }

    @PostMapping()
    public ResponseResult addUser(@RequestBody AddUserDto addUserDto){
        if(!StringUtils.hasText(addUserDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(userService.usernameExist(addUserDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(userService.phonenumberExist(addUserDto.getPhonenumber())){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        if(userService.emailExist(addUserDto.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        return userService.addUser(addUserDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteUser(@PathVariable("id")Long id){
        return userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    public ResponseResult getUserAndRole(@PathVariable("id")Long id){
        return userService.getUserAndRole(id);
    }

    @PutMapping()
    public ResponseResult updateUser(@RequestBody UpdateUserDto userDto){
        return userService.updateUserAndRoles(userDto);
    }
}
