package com.ljq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljq.domain.ResponseResult;
import com.ljq.domain.dto.ArticleDto;
import com.ljq.domain.entity.Article;
import com.ljq.domain.entity.ArticleTag;
import com.ljq.mapper.ArticleTagMapper;
import com.ljq.service.ArticleService;
import com.ljq.service.ArticleTagService;
import com.ljq.utils.BeanCopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2023-01-22 10:35:51
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

    @Autowired
    private ArticleService articleService;

    @Override
    @Transactional
    public ResponseResult uploadArticleTag(ArticleDto articleDto) {
        //添加博客
        Article article = BeanCopyUtil.copyBean(articleDto, Article.class);
        articleService.save(article);

        List<ArticleTag> articleTagList = articleDto.getTags().stream()
                .map(tagId->new ArticleTag(article.getId(),tagId))
                .collect(Collectors.toList());

        //添加博客和标签对应关系
        saveBatch(articleTagList);
        return ResponseResult.okResult();
    }
}

