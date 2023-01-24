package com.ljq.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ljq.domain.ResponseResult;
import com.ljq.domain.dto.AddRoleDto;
import com.ljq.domain.dto.RoleDto;
import com.ljq.domain.entity.Role;
import com.ljq.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResponseErrorHandler;

import java.util.List;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseResult getRole(Integer pageNum,Integer pageSize,String roleName,String status){

        return roleService.getRole(pageNum,pageSize,roleName,status);
    }

    @PutMapping("/changeStatus")
    public ResponseResult updateRoleStatus(@RequestBody RoleDto roleDto){
        return roleService.updateRoleStatus(roleDto);
    }

    @PostMapping()
    public ResponseResult addRole(@RequestBody AddRoleDto role){
        return roleService.addRole(role);
    }

    @GetMapping("/{id}")
    public ResponseResult getRoleById(@PathVariable("id")Long id){
        return roleService.getRoleById(id);
    }

    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeselect(@PathVariable("id")Long id){
        return roleService.roleMenuTreeselect(id);
    }

    @PutMapping()
    public ResponseResult updateRole(@RequestBody AddRoleDto roleDto){
        return roleService.updateRole(roleDto);
    }

    @DeleteMapping("/{id}")
    public  ResponseResult deleteRole(@PathVariable("id")Long id){
        return roleService.deleteById(id);
    }

    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        return roleService.listAllRole();
    }
}
