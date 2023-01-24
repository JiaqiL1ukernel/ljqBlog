package com.ljq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljq.constant.SystemConstant;
import com.ljq.domain.ResponseResult;
import com.ljq.domain.dto.AddRoleDto;
import com.ljq.domain.dto.RoleDto;
import com.ljq.domain.entity.Role;
import com.ljq.domain.entity.RoleMenu;
import com.ljq.domain.vo.MenuVo;
import com.ljq.domain.vo.PageVo;
import com.ljq.domain.vo.RoleMenuTreeVo;
import com.ljq.domain.vo.RoleVo;
import com.ljq.mapper.MenuMapper;
import com.ljq.mapper.RoleMapper;
import com.ljq.service.RoleMenuService;
import com.ljq.service.RoleService;
import com.ljq.utils.BeanCopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private MenuServiceImpl menuService;

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

    @Override
    @Transactional
    public ResponseResult addRole(AddRoleDto roleDto) {
        Role role = BeanCopyUtil.copyBean(roleDto, Role.class);
        save(role);
        Long id = role.getId();
        roleDto.getMenuIds().stream()
                .forEach(menuId -> {
                    RoleMenu roleMenu = new RoleMenu(id, menuId);
                    roleMenuService.save(roleMenu);
                });
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRoleById(Long id) {
        Role role = getById(id);
        RoleVo roleVo = BeanCopyUtil.copyBean(role, RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    @Override
    public ResponseResult roleMenuTreeselect(Long id) {
        List<MenuVo> menus = null;
        List<Long> checkedKeys = null;
        if(id==1){
            menus = menuMapper.selectAllRouterMenuVo();
            checkedKeys = roleMenuService.list().stream()
                    .map(roleMenu -> roleMenu.getMenuId())
                    .collect(Collectors.toList());
        }
        menus = menuMapper.roleMenuTreeselect(id);
        checkedKeys = roleMenuService.list().stream()
                .filter(roleMenu -> roleMenu.getRoleId()==id)
                .map(roleMenu -> roleMenu.getMenuId())
                .collect(Collectors.toList());
        menuService.getMenuTree(menus,0L);
        RoleMenuTreeVo roleMenuTreeVo = new RoleMenuTreeVo(menus, checkedKeys);
        return ResponseResult.okResult(roleMenuTreeVo);
    }

    @Override
    @Transactional
    public ResponseResult updateRole(AddRoleDto roleDto) {
        Role role = BeanCopyUtil.copyBean(roleDto,Role.class);
        updateById(role);
        LambdaUpdateWrapper<RoleMenu> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(RoleMenu::getRoleId,roleDto.getId());
        roleMenuService.remove(wrapper);
        List<Long> menuIds = roleDto.getMenuIds();
        menuIds.stream()
                .forEach(menuId -> {
                    RoleMenu roleMenu = new RoleMenu(roleDto.getId(), menuId);
                    roleMenuService.save(roleMenu);
                });
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult deleteById(Long id) {
        removeById(id);
        LambdaUpdateWrapper<RoleMenu> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RoleMenu::getRoleId,id);
        roleMenuService.remove(updateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllRole() {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getStatus, SystemConstant.STATE_NORMAL);
        List<Role> list = list(queryWrapper);
        return ResponseResult.okResult(list);
    }


}

