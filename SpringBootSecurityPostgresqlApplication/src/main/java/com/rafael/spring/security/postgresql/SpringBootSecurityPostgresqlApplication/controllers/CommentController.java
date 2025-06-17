package com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.controllers;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.models.Comment;
import com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.models.Tweeter;
import com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.models.User;
import com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.payload.request.CommentRequest;
import com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.payload.response.CommentResponse;
import com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.repository.CommentRepository;
import com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.repository.TweetRepository;
import com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.repository.UserRepository;


@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private UserRepository userRepository;

   @PostMapping("/add")
public ResponseEntity<?> addComment(@RequestBody CommentRequest request) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();

    System.out.println(">> Comentario recibido:");
    System.out.println("   TweetId: " + request.getTweetId());
    System.out.println("   Usuario autenticado: " + username);

    Optional<User> userOpt = userRepository.findByUsername(username);
    Optional<Tweeter> tweetOpt = tweetRepository.findById(request.getTweetId());

     if (userOpt.isEmpty()) {
        System.out.println(">> Usuario no encontrado en la base de datos");
    }
    if (tweetOpt.isEmpty()) {
        System.out.println(">> Tweet no encontrado en la base de datos");
    }

    if (userOpt.isEmpty() || tweetOpt.isEmpty()) {
        return ResponseEntity.badRequest().body("User or Tweet not found");
    }

    Comment comment = new Comment();
    comment.setContent(request.getContent());
    comment.setTweet(tweetOpt.get());
    comment.setUser(userOpt.get());

    return ResponseEntity.ok(commentRepository.save(comment));
}


   // @GetMapping("/tweet/{tweetId}")
    //public ResponseEntity<?> getCommentsByTweet(@PathVariable Long tweetId) {
      //  Optional<Tweeter> tweetOpt = tweetRepository.findById(tweetId);
       // if (tweetOpt.isEmpty()) return ResponseEntity.badRequest().body("Tweet not found");
       // return ResponseEntity.ok(commentRepository.findByTweet(tweetOpt.get()));
   // }
//}
@GetMapping("/tweet/{tweetId}")
public ResponseEntity<?> getCommentsByTweet(@PathVariable Long tweetId) {
    Optional<Tweeter> tweetOpt = tweetRepository.findById(tweetId);
    if (tweetOpt.isEmpty()) return ResponseEntity.badRequest().body("Tweet not found");

     List<Comment> comments = commentRepository.findByTweet(tweetOpt.get());

    List<CommentResponse> response = comments.stream().map(comment -> 
        new CommentResponse(
            comment.getContent(),
            comment.getCreatedAt().toString(),
            comment.getUser().getUsername()
        )
    ).collect(Collectors.toList());

    return ResponseEntity.ok(response);
}
}