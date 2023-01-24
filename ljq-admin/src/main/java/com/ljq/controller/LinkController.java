package com.ljq.controller;

import com.ljq.domain.ResponseResult;
import com.ljq.domain.entity.Link;
import com.ljq.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum,Integer pageSize,String name,String status){
        return linkService.getLinkPage(pageNum,pageSize,name,status);
    }

    @PostMapping()
    public ResponseResult addLink(@RequestBody Link link){
        linkService.save(link);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult getLinkById(@PathVariable("id")Long id){
        Link link = linkService.getById(id);
        return ResponseResult.okResult(link);
    }

    @PutMapping()
    public ResponseResult updateLink(@RequestBody Link link){
        linkService.updateById(link);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteById(@PathVariable("id")Long id){
        linkService.removeById(id);
        return ResponseResult.okResult();
    }
}
