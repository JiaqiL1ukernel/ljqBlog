package com.ljq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljq.domain.entity.UserRole;
import com.ljq.mapper.UserRoleMapper;
import com.ljq.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2023-01-24 01:31:35
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}

