package com.ljq.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CategoryDto {
    private String name;

    private String description;
    //状态0:正常,1禁用
    private String status;

}
