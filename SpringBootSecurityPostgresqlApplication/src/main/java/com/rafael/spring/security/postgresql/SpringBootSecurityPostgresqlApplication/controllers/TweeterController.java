package com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.controllers;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.models.Tweeter;
import com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.models.User;
import com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.repository.TweetRepository;
import com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.repository.UserRepository;

import jakarta.validation.Valid;



//@CrossOrigin(origins = "*", maxAge = 3600)
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

@RestController
@RequestMapping("/api/tweets")
public class TweeterController {

  @Autowired
  private TweetRepository tweetRepository;

  @Autowired
  private UserRepository userRepository;

  @GetMapping("/all")
  public Page<Tweeter> getTweet(Pageable pageable) {
    return tweetRepository.findAll(pageable);
  }

  @PostMapping("/create")
  public Tweeter createTweet(@Valid @RequestBody Tweeter tweet) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();
    System.out.println("userid : " + userId);

    User user = getValidUser(userId);
    System.out.println("user");

    System.out.println(user);
    Tweeter myTweet = new Tweeter(tweet.getTweet());
    myTweet.setPostedBy(user);
    tweetRepository.save(myTweet);

    return myTweet;
  }

  private User getValidUser(String userId) {
    Optional<User> userOpt = userRepository.findByUsername(userId);
    if (!userOpt.isPresent()) {
      throw new RuntimeException("User not found");
    }
    return userOpt.get();
  }

@CrossOrigin(origins = "http://localhost:4200")
@PostMapping("/image-url")
public ResponseEntity<?> createTweetWithImageUrl(
    @RequestParam(value = "tweet", required = false) String tweet,
    @RequestParam("imageUrl") String imageUrl
) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();

    Optional<User> userOpt = userRepository.findByUsername(username);
    if (userOpt.isEmpty()) return ResponseEntity.status(404).body("Usuario no encontrado");

    Tweeter tweetEntity = new Tweeter();
    tweetEntity.setTweet(tweet != null ? tweet : "");
    tweetEntity.setPostedBy(userOpt.get());
    tweetEntity.setImageUrl(imageUrl);

    Tweeter saved = tweetRepository.save(tweetEntity);
    return ResponseEntity.ok(saved);
}
@DeleteMapping("/{id}")
public ResponseEntity<?> deleteTweet(@PathVariable Long id) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();

    Optional<Tweeter> tweetOpt = tweetRepository.findById(id);
    if (tweetOpt.isEmpty()) return ResponseEntity.status(404).body("Tweet no encontrado");

    Tweeter tweet = tweetOpt.get();

    if (!tweet.getPostedBy().getUsername().equals(username)) {
        return ResponseEntity.status(403).body("No tienes permiso para borrar este tweet");
    }

    tweetRepository.deleteById(id);
    return ResponseEntity.ok("Tweet eliminado exitosamente");
}
}

