package com.example.newsappwithauth.repository;

import com.example.newsappwithauth.modal.NewsArticle;
import com.example.newsappwithauth.modal.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NewsRepository extends JpaRepository<NewsArticle,Long> {
    List<NewsArticle> findByStatus(Status status);

    @Modifying
    @Transactional
    @Query("UPDATE NewsArticle na SET na.status = :newStatus WHERE na.status = :oldStatus")
    void updateStatus(Status oldStatus, Status newStatus);

    @Modifying
    @Transactional
    @Query("UPDATE NewsArticle na SET na.status = :newStatus WHERE na.id = :id")
    void updateStatusById(Long id, Status newStatus);

    @Modifying
    @Transactional
    @Query("UPDATE NewsArticle na SET na.status = :newStatus WHERE na.id IN :ids")
    void updateStatusBatch(List<Long> ids, Status newStatus);
}
