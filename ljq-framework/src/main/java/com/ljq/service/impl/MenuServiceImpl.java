package com.ljq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljq.constant.SystemConstant;
import com.ljq.domain.ResponseResult;
import com.ljq.domain.entity.Menu;
import com.ljq.domain.vo.MenuTreeVo;
import com.ljq.domain.vo.MenuVo;
import com.ljq.domain.vo.SelectMenuVo;
import com.ljq.mapper.MenuMapper;
import com.ljq.service.MenuService;
import com.ljq.utils.BeanCopyUtil;
import com.ljq.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    @Override
    public ResponseResult listByMenuName(String status, String menuName) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(menuName),Menu::getMenuName,menuName);
        queryWrapper.eq(StringUtils.hasText(status),Menu::getStatus,status);
        List<SelectMenuVo> selectMenuVos = BeanCopyUtil.copyList(list(queryWrapper), SelectMenuVo.class);
        return ResponseResult.okResult(selectMenuVos);
    }

    @Override
    public ResponseResult deleteMenuById(Long menuId) {
        int size = list().stream()
                .filter(menu -> menu.getParentId().equals(menuId))
                .collect(Collectors.toList()).size();
        if(size>0){
            return new ResponseResult(500,"存在子菜单不允许删除");
        }
        menuMapper.deleteById(menuId);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult treeselect() {
        List<Menu> list = list();
        List<MenuVo> menuVos = BeanCopyUtil.copyList(list, MenuVo.class);
        List<MenuVo> menuTree = getMenuTree(menuVos, 0L);
        List<MenuTreeVo> menuTreeVos = BeanCopyUtil.copyList(menuTree, MenuTreeVo.class);
        List<MenuTreeVo> treeVos = setLabel(menuTreeVos);
        return ResponseResult.okResult(treeVos);
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

    public List<MenuTreeVo> setLabel(List<MenuTreeVo> menuTreeVos){
        return menuTreeVos;
    }


}

