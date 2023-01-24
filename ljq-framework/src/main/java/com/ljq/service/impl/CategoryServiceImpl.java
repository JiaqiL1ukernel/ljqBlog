package com.ljq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljq.constant.SystemConstant;
import com.ljq.domain.ResponseResult;
import com.ljq.domain.dto.CategoryDto;
import com.ljq.domain.dto.UpdateCatecoryDto;
import com.ljq.domain.entity.Article;
import com.ljq.domain.entity.Category;
import com.ljq.domain.vo.PageVo;
import com.ljq.domain.vo.categoryListVo;
import com.ljq.mapper.CategoryMapper;
import com.ljq.service.ArticleService;
import com.ljq.service.CategoryService;
import com.ljq.utils.BeanCopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-01-18 02:15:24
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {

        //查询文章表，状态为已发布的
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstant.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(articleWrapper);
        //获取文章分类id，去重
        Set<Long> categoryIds = articleList.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());
        //查询分类表，状态为生效
        List<Category> categoryList = listByIds(categoryIds).stream()
                .filter(category -> category.getStatus().equals(SystemConstant.CATEGORY_STATUS_NORMAL))
                .collect(Collectors.toList());

        //封装成vo
        List<categoryListVo> categoryListVos = BeanCopyUtil.copyList(categoryList, categoryListVo.class);

        //封装成ResponseResult类型返回
        return ResponseResult.okResult(categoryListVos);
    }

    @Override
    public ResponseResult getPage(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name),Category::getName,name);
        queryWrapper.eq(StringUtils.hasText(status),Category::getStatus,status);
        Page<Category> page = new Page<>(pageNum, pageSize);
        Page<Category> categoryPage = page(page, queryWrapper);
        return ResponseResult.okResult(new PageVo(categoryPage.getRecords(),categoryPage.getTotal()));
    }

    @Override
    public ResponseResult addCategory(CategoryDto categoryDto) {
        Category category = BeanCopyUtil.copyBean(categoryDto, Category.class);
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getCategoryById(Long id) {
        Category category = getById(id);
        return ResponseResult.okResult(category);
    }

    @Override
    public ResponseResult updateCategory(UpdateCatecoryDto catecoryDto) {
        Category category = BeanCopyUtil.copyBean(catecoryDto, Category.class);
        updateById(category);
        return ResponseResult.okResult();
    }


}

