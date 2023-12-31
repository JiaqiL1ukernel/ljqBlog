package com.ljq.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.ljq.domain.ResponseResult;
import com.ljq.domain.dto.CategoryDto;
import com.ljq.domain.dto.UpdateCatecoryDto;
import com.ljq.domain.entity.Category;
import com.ljq.domain.vo.CategoryVo;
import com.ljq.domain.vo.DownloadVo;
import com.ljq.enums.AppHttpCodeEnum;
import com.ljq.service.CategoryService;
import com.ljq.utils.BeanCopyUtil;
import com.ljq.utils.WebUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        try {
            //设置响应头
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取到分类数据
            List<Category> categoryList = categoryService.list();
            List<DownloadVo> downloadVos = BeanCopyUtil.copyList(categoryList, DownloadVo.class);
            //将数据写入到响应流
            EasyExcel.write(response.getOutputStream(), DownloadVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(downloadVos);

        } catch (Exception e) {
            e.printStackTrace();
            //出现异常响应报错信息给前端
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            String json = JSON.toJSONString(responseResult);
            WebUtils.renderString(response, json);
        }

    }



    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        List<Category> list = categoryService.list();
        return ResponseResult.okResult(BeanCopyUtil.copyList(list, CategoryVo.class));
    }

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum,Integer pageSize,String name,String status){
        return categoryService.getPage(pageNum,pageSize,name,status);
    }


    @PostMapping()
    public ResponseResult addCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.addCategory(categoryDto);
    }


    @GetMapping("/{id}")
    public ResponseResult getCategoryById(@PathVariable("id")Long id){
        return categoryService.getCategoryById(id);
    }

    @PutMapping()
    public  ResponseResult updateCategory(@RequestBody UpdateCatecoryDto catecoryDto){
        return categoryService.updateCategory(catecoryDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteCategory(@PathVariable("id") Long id){
        categoryService.removeById(id);
        return ResponseResult.okResult();
    }


}
