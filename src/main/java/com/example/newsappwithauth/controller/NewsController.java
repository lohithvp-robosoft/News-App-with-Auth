package com.example.newsappwithauth.controller;

import com.example.newsappwithauth.dto.response.NewsArticleResponse;
import com.example.newsappwithauth.dto.response.ResponseDTO;
import com.example.newsappwithauth.modal.NewsArticle;
import com.example.newsappwithauth.modal.Status;
import com.example.newsappwithauth.repository.NewsRepository;
import com.example.newsappwithauth.services.NewsServices;
import com.example.newsappwithauth.utils.WebClientUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private NewsServices newsServices;

    @GetMapping("/v1/test")
    public List<NewsArticleResponse> testing() throws JsonProcessingException {
        String responseJson = webClientUtil.externalGetRequestStreamAsString("https://api.worldnewsapi.com/top-news?source-country=in&language=en")
                .block();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(responseJson);
        JsonNode newsListNode = rootNode.get("top_news").get(0).get("news");
        List<NewsArticleResponse> lst = new ArrayList<>();
        for(JsonNode article: newsListNode){
            NewsArticleResponse newsArticleResponse = objectMapper.readValue(article.toString(), NewsArticleResponse.class);
            newsArticleResponse.setStatus(Status.POPULAR);
            lst.add(newsArticleResponse);
            NewsArticle newsArticle = new NewsArticle(newsArticleResponse);
            newsRepository.save(newsArticle);
        }
        return lst;
    }


    @GetMapping("/v1/news")
    public ResponseEntity<ResponseDTO<List<NewsArticleResponse>>> getAllNews(){
        return newsServices.getPopularNews();
    }
}
