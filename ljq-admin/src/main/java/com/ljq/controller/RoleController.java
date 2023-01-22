package com.ljq.controller;

import com.ljq.domain.ResponseResult;
import com.ljq.domain.dto.RoleDto;
import com.ljq.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

}
