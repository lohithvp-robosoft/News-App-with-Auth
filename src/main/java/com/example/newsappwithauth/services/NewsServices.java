package com.example.newsappwithauth.services;

import com.example.newsappwithauth.dto.response.NewsArticleResponse;
import com.example.newsappwithauth.dto.response.ResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NewsServices {

    ResponseEntity<ResponseDTO<List<NewsArticleResponse>>> getPopularNews();

    void updateNewsDatabase() throws JsonProcessingException;
}
