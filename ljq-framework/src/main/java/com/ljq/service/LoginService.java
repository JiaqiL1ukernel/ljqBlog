package com.ljq.service;

import com.ljq.domain.ResponseResult;
import com.ljq.domain.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();

    ResponseResult uploadImg(MultipartFile file);
}
