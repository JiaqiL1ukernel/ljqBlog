package com.ljq.service.impl;

import com.ljq.domain.ResponseResult;
import com.ljq.domain.entity.LoginUser;
import com.ljq.domain.entity.User;
import com.ljq.domain.vo.LoginUserVo;
import com.ljq.domain.vo.UserInfoVo;
import com.ljq.service.BlogLoginService;
import com.ljq.utils.BeanCopyUtil;
import com.ljq.utils.JwtUtil;
import com.ljq.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {
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
        redisCache.setCacheObject("bloglogin:"+id,loginUser);
        //生成token
        String jwt = JwtUtil.createJWT(id);

        //将token和userinfo封装
        UserInfoVo userInfoVo = BeanCopyUtil.copyBean(loginUser.getUser(), UserInfoVo.class);
        LoginUserVo loginUserVo = new LoginUserVo(jwt, userInfoVo);

        return ResponseResult.okResult(loginUserVo);
    }

    @Override
    public ResponseResult logout() {
        //获取token解析userId
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = loginUser.getUser().getId();

        //从redis中删除
        redisCache.deleteObject("bloglogin:"+userId);
        return ResponseResult.okResult();
    }
}
