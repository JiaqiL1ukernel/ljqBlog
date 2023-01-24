package com.ljq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljq.domain.entity.RoleMenu;
import com.ljq.mapper.RoleMenuMapper;
import com.ljq.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2023-01-23 19:09:33
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}

