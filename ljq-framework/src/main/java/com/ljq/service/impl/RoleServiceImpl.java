package com.ljq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljq.domain.ResponseResult;
import com.ljq.domain.dto.RoleDto;
import com.ljq.domain.entity.Role;
import com.ljq.domain.vo.PageVo;
import com.ljq.domain.vo.RoleVo;
import com.ljq.mapper.RoleMapper;
import com.ljq.service.RoleService;
import com.ljq.utils.BeanCopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    @Override
    public ResponseResult getRole(Integer pageNum, Integer pageSize, String roleName, String status) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(roleName),Role::getRoleName,roleName);
        queryWrapper.eq(StringUtils.hasText(status),Role::getStatus,status);
        queryWrapper.orderByAsc(Role::getRoleSort);
        Page<Role> rolePage = new Page<Role>(pageNum,pageSize);
        page(rolePage,queryWrapper);
        List<Role> records = rolePage.getRecords();
        List<RoleVo> roleVos = BeanCopyUtil.copyList(records, RoleVo.class);
        return ResponseResult.okResult(new PageVo(roleVos,rolePage.getTotal()));
    }

    @Override
    public ResponseResult updateRoleStatus(RoleDto roleDto) {
        LambdaUpdateWrapper<Role> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Role::getId,roleDto.getRoleId());
        Role role = new Role();
        role.setStatus(roleDto.getStatus());
        update(role,updateWrapper);
        return ResponseResult.okResult();
    }


}

