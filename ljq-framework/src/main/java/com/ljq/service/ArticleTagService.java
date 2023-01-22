package com.ljq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljq.domain.ResponseResult;
import com.ljq.domain.dto.ArticleDto;
import com.ljq.domain.entity.ArticleTag;


/**
 * 文章标签关联表(ArticleTag)表服务接口
 *
 * @author makejava
 * @since 2023-01-22 10:35:51
 */
public interface ArticleTagService extends IService<ArticleTag> {

    ResponseResult uploadArticleTag(ArticleDto articleDto);
}
