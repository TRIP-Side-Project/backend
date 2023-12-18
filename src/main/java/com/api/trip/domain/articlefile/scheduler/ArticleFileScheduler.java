package com.api.trip.domain.articlefile.scheduler;

import com.api.trip.domain.articlefile.service.ArticleFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
//@EnableScheduling
@RequiredArgsConstructor
public class ArticleFileScheduler {

    private final ArticleFileService articleFileService;

    //@Scheduled(cron = "0 0 3 * * *")
    public void deleteAt3EveryDay() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        articleFileService.deleteTemporaryArticleFilesBefore(yesterday);
    }
}
