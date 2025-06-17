package com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.models.Comment;
import com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.models.Tweeter;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTweet(Tweeter tweet);
}