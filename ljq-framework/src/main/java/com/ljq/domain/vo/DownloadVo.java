package com.ljq.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadVo {
    @ExcelProperty("分类名")
    private String name;
    @ExcelProperty("分类描述")
    private String description;
    @ExcelProperty("状态 1：禁用  2:正常")
    //状态0:正常,1禁用
    private String status;
}
