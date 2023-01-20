package com.ljq.filter;

import com.alibaba.fastjson.JSON;
import com.ljq.domain.ResponseResult;
import com.ljq.domain.entity.LoginUser;
import com.ljq.enums.AppHttpCodeEnum;
import com.ljq.utils.JwtUtil;
import com.ljq.utils.RedisCache;
import com.ljq.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAurhticationFilter extends OncePerRequestFilter {
    @Autowired
    private RedisCache redisCache;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        //获取token
        String token = httpServletRequest.getHeader("token");


        if (!StringUtils.hasText(token)) {
            //说明该接口不需要登录
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        //获取userid

        Claims claims;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            String json = JSON.toJSONString(responseResult);
            WebUtils.renderString(httpServletResponse, json);
            return;
        }
        String userid = claims.getSubject();
        //从redis中获取用户详细信息
        LoginUser LoginUser = redisCache.getCacheObject("bloglogin:" + userid);
        if(Objects.isNull(LoginUser)){
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            String json = JSON.toJSONString(responseResult);
            WebUtils.renderString(httpServletResponse, json);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(LoginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
