package com.example.newsappwithauth.repository;

import com.example.newsappwithauth.modal.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
}
