package com.ljq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljq.domain.entity.Tag;
import org.apache.ibatis.annotations.Mapper;


/**
 * 标签(Tag)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-20 11:17:29
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

}
