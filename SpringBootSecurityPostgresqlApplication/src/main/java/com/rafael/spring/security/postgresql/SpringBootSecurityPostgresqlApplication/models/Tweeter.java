package com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.models;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;



@Entity
@Table( name = "tweets")
public class Tweeter {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // models/Tweeter.java
  @Column(name = "image_url")
  private String imageUrl;



  
  @Size(max = 140)
  private String tweet;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnoreProperties({"password", "email", "roles", "hibernateLazyInitializer", "handler"})
  @JoinColumn(name = "posted_by", referencedColumnName = "id")
    
    private User postedBy;

  @OneToMany(mappedBy = "tweet", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private Set<TweeterReaction> likes;

  @OneToMany(mappedBy = "tweet", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private Set<Comment> comments;

    
    public Tweeter() {
    }

    public Tweeter(String tweet) {
        this.tweet = tweet;
    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public User getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(User postedBy) {
        this.postedBy = postedBy;
    }

    public String getImageUrl() {
    return imageUrl;
   }

   public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
   }
public Set<TweeterReaction> getLikes() {
        return likes;
    }

    public void setLikes(Set<TweeterReaction> likes) {
        this.likes = likes;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}

  //@OneToMany(mappedBy = "tweet", fetch = FetchType.LAZY)
  //@JsonIgnore 
  //Set<TweeterReaction> likes;

 // public Set<TweeterReaction> getLikes() {
 // return likes;
 //}

  //public void setLikes(Set<TweeterReaction> likes) {
   //this.likes = likes;
  //}



//}