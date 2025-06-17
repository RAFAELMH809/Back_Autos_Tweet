package com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.models;

public class TweetReactionDTO {
    private Long id;
    private Long tweetId;
    private Long reactionId;
    private Long userId;

    public TweetReactionDTO(TweeterReaction reaction) {
        this.id = reaction.getId();
        this.tweetId = reaction.getTweet().getId();
        this.reactionId = reaction.getReaction().getId();
        this.userId = reaction.getUser().getId();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public Long getReactionId() {
        return reactionId;
    }

    public Long getUserId() {
        return userId;
    }
}
