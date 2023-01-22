package com.ljq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljq.domain.ResponseResult;
import com.ljq.domain.dto.ArticleDto;
import com.ljq.domain.dto.UpdateArticleDto;
import com.ljq.domain.entity.Article;

public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult listArticle(Integer pageNum, Integer pageSize, ArticleDto articleDto);

    ResponseResult getArticleDetailAndTag(Long id);

    ResponseResult updateArticle(UpdateArticleDto updateArticleDto);

    ResponseResult deleteArticle(Long id);
}
