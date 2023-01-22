package com.ljq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljq.domain.entity.ArticleTag;
import org.apache.ibatis.annotations.Mapper;


/**
 * 文章标签关联表(ArticleTag)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-22 10:35:49
 */
@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

}
