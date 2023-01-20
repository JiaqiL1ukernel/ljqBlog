package com.ljq.utils;

import com.ljq.domain.entity.Article;
import com.ljq.domain.vo.HotArticleVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BeanCopyUtil {

    private BeanCopyUtil(){}

    //单个Bean拷贝
    public static <T> T copyBean(Object source,Class<T> clazz) {
        T res = null;
        try {
            res = clazz.newInstance();
            //赋值
            BeanUtils.copyProperties(source,res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    public static <T> List<T> copyList(List<?extends Object> source,Class<T> clazz){
        return source.stream()
                .map(obj -> copyBean(obj, clazz))
                .collect(Collectors.toList());
    }
}
