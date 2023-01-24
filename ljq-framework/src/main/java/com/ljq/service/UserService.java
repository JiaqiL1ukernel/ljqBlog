package com.ljq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljq.domain.ResponseResult;
import com.ljq.domain.dto.AddUserDto;
import com.ljq.domain.dto.UpdateUserDto;
import com.ljq.domain.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-01-19 10:16:16
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUser(User user);

    ResponseResult register(User user);

    ResponseResult listAllUsers(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status);

    ResponseResult addUser(AddUserDto addUserDto);

    boolean usernameExist(String userName);

    boolean phonenumberExist(String phonenumber);

    boolean nickNameExist(String nickName);

    boolean emailExist(String email);

    ResponseResult deleteUser(Long id);

    ResponseResult getUserAndRole(Long id);

    ResponseResult updateUserAndRoles(UpdateUserDto userDto);
}
