package com.ljq.controller;

import com.ljq.domain.ResponseResult;
import com.ljq.domain.dto.TagListDto;
import com.ljq.domain.entity.Tag;
import com.ljq.domain.vo.TagVo;
import com.ljq.service.TagService;
import com.ljq.utils.BeanCopyUtil;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageByTagName(pageNum,pageSize,tagListDto);
    }

    @PostMapping()
    public ResponseResult addTag(@RequestBody TagListDto tagListDto){
        return tagService.addTag(tagListDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getTagInfo(@PathVariable("id") Integer id){
        return tagService.getTagInfo(id);
    }

    @PutMapping()
    public ResponseResult updateTag(@RequestBody Tag tag){
        tagService.updateById(tag);
        return ResponseResult.okResult();
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        return ResponseResult.okResult(BeanCopyUtil.copyList(tagService.list(), TagVo.class));
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable("id")Long id){
        tagService.removeById(id);
        return ResponseResult.okResult();
    }

}
