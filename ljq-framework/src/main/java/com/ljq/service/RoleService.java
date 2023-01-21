package com.ljq.service;

import com.baomidou.mybatisplus.extension.service.IService;
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
}
