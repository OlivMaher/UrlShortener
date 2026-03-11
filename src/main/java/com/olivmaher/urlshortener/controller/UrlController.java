package com.olivmaher.urlshortener.controller;

import com.olivmaher.urlshortener.dto.ShortenRequest;
import com.olivmaher.urlshortener.entity.Url;
import com.olivmaher.urlshortener.service.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/urls")
public class UrlController {

    private final UrlService urlService;


    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody ShortenRequest req){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String shortCode = urlService.shortenUrl(req.getOriginalUrl(), email);
        return ResponseEntity.status(201).body(shortCode);
    }

    @GetMapping
    public ResponseEntity<List<Url>> userUrls(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Url> urls = urlService.getUserUrls(email);
        return ResponseEntity.ok(urls);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUrl(@PathVariable Long id){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        urlService.deleteUrl(id, email);
        return ResponseEntity.noContent().build();
    }
}
