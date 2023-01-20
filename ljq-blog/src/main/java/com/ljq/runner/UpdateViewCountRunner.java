package com.ljq.runner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ljq.domain.entity.Article;
import com.ljq.service.ArticleService;
import com.ljq.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountRunner {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;

    @Scheduled(cron = "0 0/10 * * * ? ")
    public void updateViewCount(){
        //将redis中的浏览量写入到mysql中
        //从redis中获取值
        Map<String, Integer> cacheMap = redisCache.getCacheMap("article:viewCount");
        List<Article> articleList = cacheMap.entrySet().stream()
                .map(entry -> new Article(Long.parseLong(entry.getKey()),entry.getValue().longValue()))
                .collect(Collectors.toList());

        articleService.updateBatchById(articleList);
    }

}
