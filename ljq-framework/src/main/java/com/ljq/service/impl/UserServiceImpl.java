package com.ljq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljq.domain.ResponseResult;
import com.ljq.domain.dto.AddUserDto;
import com.ljq.domain.dto.UpdateUserDto;
import com.ljq.domain.entity.Role;
import com.ljq.domain.entity.User;
import com.ljq.domain.entity.UserRole;
import com.ljq.domain.vo.PageVo;
import com.ljq.domain.vo.UserAndRoleVo;
import com.ljq.domain.vo.UserInfoVo;
import com.ljq.enums.AppHttpCodeEnum;
import com.ljq.exception.SystemException;
import com.ljq.mapper.UserMapper;
import com.ljq.service.RoleService;
import com.ljq.service.UserRoleService;
import com.ljq.service.UserService;
import com.ljq.utils.BeanCopyUtil;
import com.ljq.utils.JwtUtil;
import com.ljq.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-01-19 10:16:16
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Override
    public ResponseResult userInfo() {
        //获取userId
        Long userId = SecurityUtils.getUserId();
        //查询用户详细信息
        User user = getById(userId);
        //封装成userInfoVo返回
        UserInfoVo userInfoVo = BeanCopyUtil.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUser(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        //判断合法性
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if(usernameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }

        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());

        //写入数据库
        save(user.setPassword(encodePassword));
        return ResponseResult.okResult();

    }

    @Override
    public ResponseResult listAllUsers(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        Page<User> page = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(userName),User::getUserName,userName);
        queryWrapper.eq(StringUtils.hasText(phonenumber),User::getPhonenumber,phonenumber);
        queryWrapper.eq(StringUtils.hasText(status),User::getStatus,status);
        page(page,queryWrapper);
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    @Transactional
    public ResponseResult addUser(AddUserDto addUserDto) {
        User user = BeanCopyUtil.copyBean(addUserDto, User.class);
        user.setPassword(passwordEncoder.encode(addUserDto.getPassword()));
        save(user);
        insertUserRole(addUserDto.getRoleIds(),user.getId());
        return ResponseResult.okResult();
    }

    private void insertUserRole(List<Long> roleIds, Long id) {
        if(roleIds!=null && roleIds.size()>0){
            List<UserRole> userRoles = roleIds.stream()
                    .map(roleId -> new UserRole(id, roleId)).collect(Collectors.toList());
            userRoleService.saveBatch(userRoles);
        }
    }


    @Override
    public boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,nickName);
        return count(queryWrapper)>0;
    }

    @Override
    public boolean emailExist(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email);
        return count(queryWrapper) > 0;
    }

    @Override
    @Transactional
    public ResponseResult deleteUser(Long id) {
        removeById(id);
        LambdaUpdateWrapper<UserRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserRole::getUserId,id);
        userRoleService.remove(updateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserAndRole(Long id) {
        //查询rolesId
        LambdaQueryWrapper<UserRole> UserRolequeryWrapper = new LambdaQueryWrapper<>();
        List<Long> roleIds = userRoleService.list(UserRolequeryWrapper.eq(UserRole::getUserId,id)).stream()
                .map(userRole -> userRole.getRoleId())
                .collect(Collectors.toList());

        //获取id对应的role信息
        LambdaQueryWrapper<Role> roleQueryWrapper = new LambdaQueryWrapper<>();
        roleQueryWrapper.in(roleIds.size()>0,Role::getId,roleIds);
        List<Role> roles = roleService.list(roleQueryWrapper);

        //获取user信息
        User user = getById(id);

        //返回
        return ResponseResult.okResult(new UserAndRoleVo(roleIds,roles,user));
    }

    @Override
    @Transactional
    public ResponseResult updateUserAndRoles(UpdateUserDto userDto) {
        //更新user表信息
        updateById(BeanCopyUtil.copyBean(userDto,User.class));
        //更新user_role表信息
            //根据userId删除user_role表项
        Long id = userDto.getId();
        LambdaUpdateWrapper<UserRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserRole::getUserId,id);
        userRoleService.remove(updateWrapper);
            //向user_role表中插入对应的roleIds
        List<Long> roleId = userDto.getRoleIds();
        List<UserRole> userRoles = roleId.stream()
                .map(rId -> new UserRole(id, rId)).collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }

    @Override
    public boolean usernameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        return count(queryWrapper)>0;
    }
    @Override
    public boolean phonenumberExist(String phonenumber) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhonenumber,phonenumber);
        return count(queryWrapper)>0;
    }


}

