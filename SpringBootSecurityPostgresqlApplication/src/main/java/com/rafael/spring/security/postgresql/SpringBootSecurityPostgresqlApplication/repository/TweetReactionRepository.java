package com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.models.Tweeter;
import com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.models.TweeterReaction;
import com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.models.User;

@Repository
public interface TweetReactionRepository extends JpaRepository<TweeterReaction, Long> {
    Optional<TweeterReaction> findByUserAndTweet(User user, Tweeter tweet);
    List<TweeterReaction> findByTweet(Tweeter tweet);
}