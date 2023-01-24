package com.ljq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljq.domain.ResponseResult;
import com.ljq.domain.entity.Link;
import org.springframework.stereotype.Service;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-01-18 04:52:57
 */

public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult getLinkPage(Integer pageNum, Integer pageSize, String name, String status);
}
