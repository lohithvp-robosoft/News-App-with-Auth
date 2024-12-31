package com.example.newsappwithauth.modal;

import com.example.newsappwithauth.dto.response.NewsArticleResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "news_articles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsArticle {
    @Id
    private Long id;

    @Column(name = "title", columnDefinition = "TEXT")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(name = "url", columnDefinition = "TEXT")
    private String url;

    @Column(name = "image", columnDefinition = "TEXT")
    private String image;

    private String publish_date;
    private String author;

    @Enumerated(EnumType.STRING)
    private Status status;


    public NewsArticle(NewsArticleResponse newsArticleResponse) {
        this.id = newsArticleResponse.getId();
        this.title = newsArticleResponse.getTitle();
        this.content = newsArticleResponse.getContent();
        this.summary = newsArticleResponse.getSummary();
        this.url = newsArticleResponse.getUrl();
        this.image = newsArticleResponse.getImage();
        this.publish_date = newsArticleResponse.getPublish_date();
        this.author = newsArticleResponse.getAuthor();
        this.status = newsArticleResponse.getStatus();
    }
}
