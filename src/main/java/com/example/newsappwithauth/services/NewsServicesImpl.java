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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    @Value("${url.updateNewsDb}")
    private String urlForUpdateDb;

    @Value("${url.searchNews}")
    private String urlForSearch;


    @Override
    public ResponseEntity<ResponseDTO<List<NewsArticleResponse>>> getPopularNews() {
        return responseUtil.successResponse(geNewsFromDB(newsRepository.findByStatus(Status.POPULAR)));
    }

    @Transactional
    @Override
    public void updateNewsDatabase() throws JsonProcessingException {
        long dbSize = newsRepository.count();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode topNews = getResponseJsonFromURL(urlForUpdateDb, "top_news");

        List<Long> idsToUpdate = new ArrayList<>();

        newsRepository.updateStatus(Status.TOP_HEADLINES, Status.POPULAR);

        for (JsonNode topNewsList : topNews) {
            JsonNode newsList = topNewsList.get("news");
            if (!newsList.isEmpty()) {
                JsonNode latestNews = newsList.get(newsList.size() - 1);
                log.debug("LatestNews {}", latestNews);
                NewsArticleResponse newsArticleResponse = objectMapper.readValue(latestNews.toString(), NewsArticleResponse.class);
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
            } else {
                log.warn("Empty news list encountered");
            }
        }
        if (!idsToUpdate.isEmpty()) {
            newsRepository.updateStatusBatch(idsToUpdate, Status.TOP_HEADLINES);
        }
        log.info("{} new News Articles are added", newsRepository.count() - dbSize);
    }

    @Override
    public ResponseEntity<ResponseDTO<List<NewsArticleResponse>>> getTopHeadlinesNews() {
        return responseUtil.successResponse(geNewsFromDB(newsRepository.findByStatus(Status.TOP_HEADLINES)));
    }

    @Override
    public ResponseEntity<ResponseDTO<List<NewsArticleResponse>>> getSearchedNews(String topic) throws JsonProcessingException {

        List<NewsArticleResponse> newsList = geNewsFromDB(newsRepository.findByTitleContainingIgnoreCase(topic));

        if (newsList.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode searchedNewsList = getResponseJsonFromURL(String.format(urlForSearch, topic), "news");

            for (JsonNode news : searchedNewsList) {
                NewsArticleResponse seachedNewsArticleResponse = objectMapper.readValue(news.toString(), NewsArticleResponse.class);
                newsList.add(seachedNewsArticleResponse);
            }
        }
        return responseUtil.successResponse(newsList);
    }

    @Override
    public ResponseEntity<ResponseDTO<NewsArticleResponse>> getOneNewsArticleById(long id) {
        Optional<NewsArticle> newsArticle = newsRepository.findById(id);
        return newsArticle.map(article -> responseUtil.successResponse(new NewsArticleResponse(article))).orElse(responseUtil.errorResponse("News Not found"));
    }

    public List<NewsArticleResponse> geNewsFromDB(List<NewsArticle> newsListFromDB) {
        List<NewsArticleResponse> newsList = new ArrayList<>();
        newsListFromDB.forEach(article ->
                {
                    NewsArticleResponse response = new NewsArticleResponse(article);
                    newsList.add(response);
                }
        );
        return newsList;
    }

    public JsonNode getResponseJsonFromURL(String url, String key) throws JsonProcessingException {
        String responseJson = webClientUtil.externalGetRequestStreamAsString(url).block();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(responseJson);
        return rootNode.get(key);
    }
}
