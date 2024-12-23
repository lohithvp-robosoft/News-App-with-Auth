package com.example.newsappwithauth.dto;

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
    private String img;
    private String publish_date;
    private String author;
}
