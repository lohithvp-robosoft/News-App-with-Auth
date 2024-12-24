package com.example.newsappwithauth.dto.response;

import com.example.newsappwithauth.modal.NewsArticle;
import com.example.newsappwithauth.modal.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class NewsArticleResponse {

    private Long id;
    private String title;

    @JsonProperty("text")
    private String content;
    private String summary;
    private String url;
    private String image;
    private String publish_date;
    private String author;
    private Status status;

    public NewsArticleResponse(NewsArticle newsArticle){
        this.id = newsArticle.getId();
        this.title = newsArticle.getTitle();
        this.content = newsArticle.getContent();
        this.summary = newsArticle.getSummary();
        this.url = newsArticle.getUrl();
        this.image = newsArticle.getImage();
        this.publish_date = newsArticle.getPublish_date();
        this.author = newsArticle.getAuthor();
        this.status = newsArticle.getStatus();
    }
}
