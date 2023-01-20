package com.ljq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljq.constant.SystemConstant;
import com.ljq.domain.ResponseResult;
import com.ljq.domain.entity.Comment;
import com.ljq.domain.vo.CommentVo;
import com.ljq.domain.vo.PageVo;
import com.ljq.enums.AppHttpCodeEnum;
import com.ljq.exception.SystemException;
import com.ljq.mapper.CommentMapper;
import com.ljq.service.CommentService;
import com.ljq.service.UserService;
import com.ljq.utils.BeanCopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-01-19 09:23:08
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private UserService userService;

    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        //根据articleId查询根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemConstant.ARTICLE_COMMENT.equals(commentType),Comment::getArticleId,articleId);
        queryWrapper.eq(Comment::getRootId,-1);
        queryWrapper.eq(Comment::getType,commentType);
        //分页
        Page<Comment> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<Comment> list = page.getRecords();

        List<CommentVo> commentVos = toCommentVoList(list);
        //设置commentVo中的children

        List<CommentVo> result = commentVos.stream()
                .map(commentVo -> commentVo.setChildren(getChildren(commentVo))).collect(Collectors.toList());

        return ResponseResult.okResult(new PageVo(result,page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        //校验评论合法性
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }

        //加入到数据库
        save(comment);
        return ResponseResult.okResult();
    }

    private List<CommentVo> toCommentVoList(List<Comment> list) {
        List<CommentVo> res = BeanCopyUtil.copyList(list, CommentVo.class);
        for (CommentVo commentVo : res) {
            //设置userName
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUserName(nickName);

            //设置toCommentUserName
            if(commentVo.getToCommentUserId()!=-1){
                String name = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(name);
            }

        }
        return res;
    }

    public List<CommentVo> getChildren(CommentVo commentVo){
        Long id = commentVo.getId();
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,id);
        queryWrapper.orderByDesc(Comment::getCreateTime);
        List<Comment> commentChildrens = list(queryWrapper);
        List<CommentVo> commentVos = toCommentVoList(commentChildrens);
        return commentVos;

    }
}

