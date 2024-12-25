package com.example.newsappwithauth.services.scheduler;

import com.example.newsappwithauth.services.NewsServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Log4j2
public class NewsScheduler {

    @Autowired
    private NewsServices newsServices;

    @Scheduled(cron = "0 0 */1 * * *")
    public void scheduleNewsUpdate() throws JsonProcessingException {
        log.info("Data is Updated at {}", LocalDateTime.now());
        newsServices.updateNewsDatabase();
    }

    @PostConstruct
    public void initializeNewsData() throws JsonProcessingException {
        log.info("Data is fetched at Startup");
        newsServices.updateNewsDatabase();
    }

}
