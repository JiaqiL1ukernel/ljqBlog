package com.ljq.domain.dto;

import com.ljq.domain.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {

    //标题
    private String title;
    //文章内容
    private String content;
    //文章摘要
    private String summary;
    //所属分类id
    private Long categoryId;

    private List<Long> tags;

    private String isTop;
    //状态（0已发布，1草稿）
    private String status;

    private String isComment;

    private String thumbnail;

}
