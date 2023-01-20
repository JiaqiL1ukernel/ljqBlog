package com.ljq.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkVo {
    //网站地址
    private String address;

    private String description;

    private Long id;

    private String logo;

    private String name;
}
