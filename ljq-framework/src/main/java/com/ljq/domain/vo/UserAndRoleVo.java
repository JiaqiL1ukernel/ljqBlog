package com.ljq.domain.vo;

import com.ljq.domain.entity.Role;
import com.ljq.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserAndRoleVo {
    private List<Long> roleIds;

    private List<Role> roles;

    private User user;

}
