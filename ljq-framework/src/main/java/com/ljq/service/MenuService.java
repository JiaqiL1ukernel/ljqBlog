package com.ljq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljq.domain.ResponseResult;
import com.ljq.domain.entity.Menu;
import com.ljq.domain.vo.MenuVo;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-01-20 13:18:57
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long userId);

    List<MenuVo> selectRouterMenuTreeByUserId(Long userId);

    ResponseResult listByMenuName(String status, String menuName);

    ResponseResult deleteMenuById(Long menuId);

    ResponseResult treeselect();
}
