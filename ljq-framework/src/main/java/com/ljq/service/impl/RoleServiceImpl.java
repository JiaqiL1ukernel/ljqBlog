package com.ljq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljq.domain.entity.Role;
import com.ljq.mapper.RoleMapper;
import com.ljq.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-01-20 13:25:59
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<String> selectRoleKeyByUserId(Long userId) {
        return roleMapper.selectRoleKeyByUserId(userId);
    }
}

