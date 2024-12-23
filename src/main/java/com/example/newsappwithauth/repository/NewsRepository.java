package com.example.newsappwithauth.repository;

import com.example.newsappwithauth.modal.NewsArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<NewsArticle,Long> {
}
