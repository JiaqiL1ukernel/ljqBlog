package com.ljq.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CommentVo {


    private Long articleId;
    private List<CommentVo> children;
    private String content;
    private Long createBy;
    private Date createTime;
    private Long id;
    private Long rootId;
    private Long toCommentId;
    private Long toCommentUserId;
    private String toCommentUserName;
    private String userName;


}
