package com.ljq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljq.domain.ResponseResult;
import com.ljq.domain.dto.AddRoleDto;
import com.ljq.domain.dto.RoleDto;
import com.ljq.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-01-20 13:25:59
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long userId);

    ResponseResult getRole(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult updateRoleStatus(RoleDto roleDto);

    ResponseResult addRole(AddRoleDto role);

    ResponseResult getRoleById(Long id);

    ResponseResult roleMenuTreeselect(Long id);

    ResponseResult updateRole(AddRoleDto roleDto);

    ResponseResult deleteById(Long id);

    ResponseResult listAllRole();
}
