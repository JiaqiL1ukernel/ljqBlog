package com.ljq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljq.domain.entity.Menu;
import com.ljq.domain.vo.MenuVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-20 13:18:56
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long userId);

    List<MenuVo> selectAllRouterMenu();

    List<MenuVo> selectRouterMenuByUserId(Long userId);

    List<MenuVo> selectAllRouterMenuVo();

    List<MenuVo> roleMenuTreeselect(Long id);
}
