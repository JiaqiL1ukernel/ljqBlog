package com.ljq.service.impl;

import com.ljq.domain.ResponseResult;
import com.ljq.domain.entity.LoginUser;
import com.ljq.domain.entity.User;
import com.ljq.domain.vo.LoginUserVo;
import com.ljq.domain.vo.UserInfoVo;
import com.ljq.service.BlogLoginService;
import com.ljq.service.LoginService;
import com.ljq.utils.BeanCopyUtil;
import com.ljq.utils.JwtUtil;
import com.ljq.utils.RedisCache;
import com.ljq.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断密码校验是否通过
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //获取userid,将用户详细信息存入redis
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String id = loginUser.getUser().getId().toString();
        redisCache.setCacheObject("login:"+id,loginUser);
        //生成token
        String jwt = JwtUtil.createJWT(id);

        //将token封装
        HashMap<String, String> map = new HashMap<>();
        map.put("token",jwt);

        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        Long userId = SecurityUtils.getUserId();
        redisCache.deleteObject("login:"+userId);
        return ResponseResult.okResult();

    }

    @Override
    public ResponseResult uploadImg(MultipartFile file) {



        return ResponseResult.okResult();
    }
}
