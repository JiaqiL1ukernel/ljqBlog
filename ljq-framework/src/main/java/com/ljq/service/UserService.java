package com.ljq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljq.domain.ResponseResult;
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
}
