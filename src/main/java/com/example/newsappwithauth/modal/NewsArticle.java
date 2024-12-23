package com.example.newsappwithauth.modal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    private String title;
    private String content;
    private String summary;
    private String url;
    private String img;
    private String publish_date;
    private String author;
}
