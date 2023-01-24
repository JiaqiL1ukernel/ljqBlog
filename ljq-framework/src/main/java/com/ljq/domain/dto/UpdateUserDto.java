package com.ljq.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UpdateUserDto {
    private Long id;

    //用户名
    private String userName;
    //昵称
    private String nickName;

    private String status;
    //邮箱
    private String email;

    private String sex;

    private List<Long> roleIds;

}
