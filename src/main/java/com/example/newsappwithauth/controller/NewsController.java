package com.example.newsappwithauth.controller;

import com.example.newsappwithauth.dto.NewsArticleResponse;
import com.example.newsappwithauth.repository.NewsRepository;
import com.example.newsappwithauth.utils.WebClientUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class NewsController {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private WebClientUtil webClientUtil;

    @GetMapping("/v1/news")
    public List<NewsArticleResponse> testing() throws JsonProcessingException {
        String responseJson = webClientUtil.externalGetRequestStreamAsString("https://api.worldnewsapi.com/top-news?source-country=in&language=en")
                .block();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(responseJson);
        JsonNode newsListNode = rootNode.get("top_news").get(0).get("news");
        List<NewsArticleResponse> lst = new ArrayList<>();
        for(JsonNode article: newsListNode){
            NewsArticleResponse newsArticleResponse = objectMapper.readValue(article.toString(), NewsArticleResponse.class);
            lst.add(newsArticleResponse);
        }
        return lst;
    }
}
