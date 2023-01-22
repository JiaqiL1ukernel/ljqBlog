package com.ljq.controller;

import com.ljq.domain.ResponseResult;
import com.ljq.domain.dto.ArticleDto;
import com.ljq.domain.dto.UpdateArticleDto;
import com.ljq.domain.entity.Menu;
import com.ljq.service.ArticleService;
import com.ljq.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, ArticleDto articleDto){

        return articleService.listArticle(pageNum,pageSize,articleDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticleDetailAndTag(@PathVariable("id") Long id){
        return articleService.getArticleDetailAndTag(id);
    }

    @PutMapping()
    public ResponseResult updateArticle(@RequestBody UpdateArticleDto updateArticleDto){
        return articleService.updateArticle(updateArticleDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable("id")Long id){
        return articleService.deleteArticle(id);
    }


}
