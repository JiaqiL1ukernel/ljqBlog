package com.ljq.controller;

import com.ljq.domain.ResponseResult;
import com.ljq.domain.dto.ArticleDto;
import com.ljq.domain.entity.Article;
import com.ljq.domain.entity.Category;
import com.ljq.domain.vo.CategoryVo;
import com.ljq.service.ArticleService;
import com.ljq.service.ArticleTagService;
import com.ljq.service.CategoryService;
import com.ljq.utils.BeanCopyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content")
public class WriteBlogController {


    @Autowired
    private ArticleTagService articleTagService;



    @PostMapping("/article")
    public ResponseResult uploadArticle(@RequestBody ArticleDto articleDto){

        return articleTagService.uploadArticleTag(articleDto);
    }

}
