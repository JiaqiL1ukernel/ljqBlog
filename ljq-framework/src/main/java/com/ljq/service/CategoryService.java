package com.ljq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljq.domain.ResponseResult;
import com.ljq.domain.entity.Category;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-01-18 02:15:23
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();
}
