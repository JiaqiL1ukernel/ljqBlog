package com.ljq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljq.constant.SystemConstant;
import com.ljq.domain.entity.Menu;
import com.ljq.domain.vo.MenuVo;
import com.ljq.mapper.MenuMapper;
import com.ljq.service.MenuService;
import com.ljq.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-01-20 13:18:57
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<String> selectPermsByUserId(Long userId) {
        if(userId==1){
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType, SystemConstant.MENU,SystemConstant.BUTTON);
            queryWrapper.eq(Menu::getStatus,SystemConstant.STATE_NORMAL);
            return list(queryWrapper).stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
        }

        return menuMapper.selectPermsByUserId(userId);
    }

    @Override
    public List<MenuVo> selectRouterMenuTreeByUserId(Long userId) {
        List<MenuVo> menus = null;
        if(SecurityUtils.isAdmin()){
            menus = getBaseMapper().selectAllRouterMenu();
        }else {
            menus = getBaseMapper().selectRouterMenuByUserId(userId);
        }
        menus = getMenuTree(menus,0L);
        return menus;
    }


    //给menus的子菜单children属性赋值
    private List<MenuVo> getMenuTree(List<MenuVo> menus,Long parentId) {
        return menus.stream()
                .filter(menu->menu.getParentId().equals(parentId))
                .map(menu->menu.setChildren(getChildren(menu,menus)))
                .collect(Collectors.toList());
    }

    private List<MenuVo> getChildren(MenuVo menu, List<MenuVo> menus) {
        return menus.stream()
                .filter(m->m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
    }


}

