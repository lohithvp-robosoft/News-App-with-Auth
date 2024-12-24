package com.example.newsappwithauth.services;

import com.example.newsappwithauth.dto.response.NewsArticleResponse;
import com.example.newsappwithauth.dto.response.ResponseDTO;
import com.example.newsappwithauth.modal.NewsArticle;
import com.example.newsappwithauth.modal.Status;
import com.example.newsappwithauth.repository.NewsRepository;
import com.example.newsappwithauth.utils.ResponseUtil;
import com.example.newsappwithauth.utils.WebClientUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Log4j2
@Service
public class NewsServicesImpl implements NewsServices {

    @Autowired
    NewsRepository newsRepository;


    @Autowired
    ResponseUtil responseUtil;

    @Autowired
    WebClientUtil webClientUtil;

    private static final int BATCH_SIZE = 50;

    @Override
    public ResponseEntity<ResponseDTO<List<NewsArticleResponse>>> getPopularNews() {
        List<NewsArticle> responseList = newsRepository.findByStatus(Status.POPULAR);

        List<NewsArticleResponse> newsList = new ArrayList<>();
        log.info(newsList);
        responseList.forEach(article ->
                {
                    NewsArticleResponse response = new NewsArticleResponse(article);
                    newsList.add(response);
                }
        );
        return responseUtil.successResponse(newsList);
    }

    @Transactional
    @Override
    public void updateNewsDatabase() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String responseJson = webClientUtil.externalGetRequestStreamAsString("https://api.worldnewsapi.com/top-news?source-country=in&language=en").block();

        JsonNode rootNode = objectMapper.readTree(responseJson);
        JsonNode newsListNode = rootNode.get("top_news").get(0).get("news");
        newsRepository.updateStatus(Status.TOP_HEADLINES,Status.POPULAR);

//        for(JsonNode article : newsListNode){
//            NewsArticleResponse newsArticleResponse = objectMapper.readValue(article.toString(),NewsArticleResponse.class);
//            if(newsRepository.existsById(newsArticleResponse.getId())){
//                newsRepository.updateStatusById(newsArticleResponse.getId(),Status.TOP_HEADLINES);
//            }else{
//                newsArticleResponse.setStatus(Status.TOP_HEADLINES);
//                NewsArticle newsArticle = new NewsArticle(newsArticleResponse);
//                newsRepository.save(newsArticle);
//            }
//        }

        List<Long> idsToUpdate = new ArrayList<>();
        for (JsonNode article : newsListNode) {
            NewsArticleResponse newsArticleResponse = objectMapper.readValue(article.toString(), NewsArticleResponse.class);

            if (newsRepository.existsById(newsArticleResponse.getId())) {
                idsToUpdate.add(newsArticleResponse.getId());

                if (idsToUpdate.size() >= BATCH_SIZE) {
                    newsRepository.updateStatusBatch(idsToUpdate, Status.TOP_HEADLINES);
                    idsToUpdate.clear();
                }
            } else {

                newsArticleResponse.setStatus(Status.TOP_HEADLINES);
                NewsArticle newsArticle = new NewsArticle(newsArticleResponse);
                newsRepository.save(newsArticle);
            }
        }

        if (!idsToUpdate.isEmpty()) {
            newsRepository.updateStatusBatch(idsToUpdate, Status.TOP_HEADLINES);
        }
    }
}
