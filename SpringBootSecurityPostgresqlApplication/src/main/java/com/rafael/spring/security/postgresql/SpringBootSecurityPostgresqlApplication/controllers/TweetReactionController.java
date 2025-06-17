package com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.controllers;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.models.Reaction;
import com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.models.TweetReactionDTO;
import com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.models.Tweeter;
import com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.models.TweeterReaction;
import com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.models.User;
import com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.payload.request.TweetReactionRequest;
import com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.repository.ReactionRepository;
import com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.repository.TweetReactionRepository;
import com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.repository.TweetRepository;
import com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.repository.UserRepository;

import jakarta.validation.Valid;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/reactions")

public class TweetReactionController {
    @Autowired
    private TweetReactionRepository tweetReactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TweetRepository tweetRepository;
    @Autowired
    private ReactionRepository reactionRepository;

    @GetMapping("/all")
    public Page<TweeterReaction> getTweet(Pageable pageable) {
        return tweetReactionRepository.findAll(pageable);
    }

    @PostMapping("/create")
    public TweeterReaction createReaction(@Valid @RequestBody TweetReactionRequest tweetReaction) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        User user = getValidUser(userId);
        Tweeter tweet = getValidTweet(tweetReaction.getTweetId());
        Reaction reaction = getValidReaction(tweetReaction.getReactionId());

        Optional<TweeterReaction> existing = tweetReactionRepository.findByUserAndTweet(user, tweet);

        TweeterReaction toSave;

        if (existing.isPresent()) {
            TweeterReaction existingReaction = existing.get();

            // Si ya es la misma reacción, retorna tal cual
            if (existingReaction.getReaction().getId().equals(reaction.getId())) {
                return existingReaction;
            }

            // Actualiza la reacción
            existingReaction.setReaction(reaction);
            existingReaction.setReactionId(reaction.getId());
            toSave = existingReaction;

        } else {
            TweeterReaction newReaction = new TweeterReaction();
            newReaction.setUser(user);
            newReaction.setTweet(tweet);
            newReaction.setReaction(reaction);

            newReaction.setUserId(user.getId());
            newReaction.setTweetId(tweet.getId());
            newReaction.setReactionId(reaction.getId());

            toSave = newReaction;
        }

        return tweetReactionRepository.save(toSave);
    }

    // Contar cuántas reacciones tiene un tweet por tipo
    @GetMapping("/count/{tweetId}")
    public Map<String, Integer> countReactions(@PathVariable Long tweetId) {
        Tweeter tweet = getValidTweet(tweetId);

        List<TweeterReaction> all = tweetReactionRepository.findByTweet(tweet);

        Map<String, Integer> count = new HashMap<>();
        count.put("LIKE", 0);
        count.put("DISLIKE", 0);

        for (TweeterReaction r : all) {
            String desc = r.getReaction().getDescription().name();
            count.put(desc, count.get(desc) + 1);
        }

        return count;
    }

    private User getValidUser(String userId) {
        Optional<User> userOpt = userRepository.findByUsername(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }
        return userOpt.get();
    }

    private Tweeter getValidTweet(Long tweetId) {
        Optional<Tweeter> tweetOpt = tweetRepository.findById(tweetId);
        if (!tweetOpt.isPresent()) {
            throw new RuntimeException("Tweet not found");
        }
        return tweetOpt.get();
    }

    private Reaction getValidReaction(Long reactionId) {
        Optional<Reaction> reactionOpt = reactionRepository.findById(reactionId);
        if (!reactionOpt.isPresent()) {
            throw new RuntimeException("Reaction not found");
        }
        return reactionOpt.get();
    }


@GetMapping("/tweet/{tweetId}")
public ResponseEntity<List<TweetReactionDTO>> getReactionsByTweet(@PathVariable Long tweetId) {
    Tweeter tweet = getValidTweet(tweetId);
    List<TweeterReaction> reactions = tweetReactionRepository.findByTweet(tweet);
    
    List<TweetReactionDTO> response = reactions.stream()
            .map(TweetReactionDTO::new)
            .collect(Collectors.toList());

    return ResponseEntity.ok(response);
}
}
