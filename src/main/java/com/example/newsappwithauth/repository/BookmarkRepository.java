package com.example.newsappwithauth.repository;

import com.example.newsappwithauth.modal.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByUser_IdAndNewsArticle_Id(Long userId, Long articleId);
    List<Bookmark> findByUser_Id(Long userId);
}
