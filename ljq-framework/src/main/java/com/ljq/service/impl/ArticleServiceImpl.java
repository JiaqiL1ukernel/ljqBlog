package com.ljq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljq.constant.SystemConstant;
import com.ljq.domain.ResponseResult;
import com.ljq.domain.entity.Article;
import com.ljq.domain.entity.Category;
import com.ljq.domain.vo.ArticleDetailVo;
import com.ljq.domain.vo.ArticleListVo;
import com.ljq.domain.vo.HotArticleVo;
import com.ljq.domain.vo.PageVo;
import com.ljq.mapper.ArticleMapper;
import com.ljq.mapper.CategoryMapper;
import com.ljq.service.ArticleService;
import com.ljq.service.CategoryService;
import com.ljq.utils.BeanCopyUtil;
import com.ljq.utils.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {



    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult hotArticleList() {
//        List<Article> hotArticles = list().stream()
//                .sorted((o1, o2) -> (int) (o1.getViewCount() - o2.getViewCount()))
//                .filter(article -> article.getStatus() == "1")
//                .filter(article -> article.getDelFlag() == 0)
//                .limit(10)
//                .collect(Collectors.toList());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是已发布文章
        queryWrapper.eq(Article::getStatus, SystemConstant.ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多查询10条
        Page<Article> page = new Page<>(1,10);
        page(page,queryWrapper);
        List<Article> hotArticles = page.getRecords();
        hotArticles = hotArticles.stream()
                .map(article -> {
                    Integer viewCount = redisCache.getCacheMapValue("article:viewCount",article.getId().toString());
                    return article.setViewCount(viewCount.longValue());
                }).collect(Collectors.toList());
        List<HotArticleVo> vos = BeanCopyUtil.copyList(hotArticles, HotArticleVo.class);
        return ResponseResult.okResult(vos);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //如果没有categoryId
        queryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,Article::getCategoryId,categoryId);

        //条件：正式发布的文章 置顶的文章要显示在最前面，按照is_top字段进行排序（降序）
        queryWrapper.eq(Article::getStatus,SystemConstant.ARTICLE_STATUS_NORMAL);
        queryWrapper.orderByDesc(Article::getIsTop);

        //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);

        List<Article> articles = page.getRecords();
        articles = articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .map(article -> {
                    Integer viewCount = redisCache.getCacheMapValue("article:viewCount",article.getId().toString());
                    return article.setViewCount(viewCount.longValue());
                })
                .collect(Collectors.toList());

        //封装成vo对象
        List<ArticleListVo> list = BeanCopyUtil.copyList(articles, ArticleListVo.class);

        PageVo pageVo = new PageVo(list, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);

        //从redis中获取浏览量
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount",id.toString());
        article.setViewCount(viewCount.longValue());
        Category category = categoryService.getById(article.getCategoryId());
        //根据分类id查询分类名
        if (category!=null) {
            article.setCategoryName(category.getName());
        }
        //封装成vo
        ArticleDetailVo articleDetailVo = BeanCopyUtil.copyBean(article, ArticleDetailVo.class);

        return ResponseResult.okResult(articleDetailVo);

    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        String key = "article:viewCount";

        redisCache.incrementCacheMapValue(key,id.toString(),1);
        return ResponseResult.okResult();
    }
}
