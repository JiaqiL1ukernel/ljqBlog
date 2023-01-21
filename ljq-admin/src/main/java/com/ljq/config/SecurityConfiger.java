package com.ljq.config;


import com.ljq.filter.JwtAurhticationFilter;
import com.ljq.handler.security.AccessDeniedHandlerImpl;
import com.ljq.handler.security.AuthenticationEntryPointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiger extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAurhticationFilter jwtAurhticationFilter;

    //认证失败异常处理器
    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    //授权失败异常处理器
    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
//                .antMatchers("/login").anonymous()
//                .antMatchers("/logout").authenticated()
//                .antMatchers("/user/userInfo").authenticated()
//                .antMatchers("/upload").authenticated()
                // 除上面外的所有请求全部不需要认证即可访问
                .antMatchers("/user/login").anonymous()
                .anyRequest().authenticated();

        //将过滤器加入到UserNameAndPasswordFilter之前
        http.addFilterBefore(jwtAurhticationFilter,UsernamePasswordAuthenticationFilter.class);

        http.logout().disable();

        //允许跨域
        http.cors();

        //配置异常处理器
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
