package com.ljq.service;

import com.ljq.domain.ResponseResult;
import com.ljq.domain.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
