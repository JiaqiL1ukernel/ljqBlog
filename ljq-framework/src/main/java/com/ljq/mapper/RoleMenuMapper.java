package com.ljq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljq.domain.entity.RoleMenu;
import org.apache.ibatis.annotations.Mapper;


/**
 * 角色和菜单关联表(RoleMenu)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-23 19:09:31
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

}
