package com.ljq.controller;

import com.ljq.domain.ResponseResult;
import com.ljq.domain.entity.Menu;
import com.ljq.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public ResponseResult list(String status,String menuName){
        return menuService.listByMenuName(status,menuName);
    }

    @GetMapping("/{id}")
    public ResponseResult getMenuById(@PathVariable("id")Long id){
        Menu menu = menuService.getById(id);
        return ResponseResult.okResult(menu);
    }

    @PostMapping()
    public ResponseResult addMenu(@RequestBody Menu menu){
        menuService.save(menu);
        return ResponseResult.okResult();
    }

    @PutMapping()
    public ResponseResult updateMenu(@RequestBody Menu menu){
        if(menu.getId().equals(menu.getParentId())){
            return new ResponseResult(500,"修改菜单'写博文'失败，上级菜单不能选择自己");
        }
        menuService.updateById(menu);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{menuId}")
    public ResponseResult deleteMunu(@PathVariable("menuId")Long menuId){
        return menuService.deleteMenuById(menuId);
    }


    @GetMapping("/treeselect")
    public ResponseResult treeselect(){
        return menuService.treeselect();
    }

}
