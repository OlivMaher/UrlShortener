package com.olivmaher.urlshortener.controller;

import com.olivmaher.urlshortener.service.UrlService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class RedirectController {

    private final UrlService urlService;

    public RedirectController(UrlService urlService) {
        this.urlService = urlService;
    }


    @GetMapping("/{shortCode}")
    public ResponseEntity<String> redirect(@PathVariable String shortCode){
        String originalUrl = urlService.getOriginalUrl(shortCode);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(originalUrl));
        return ResponseEntity.status(302).headers(headers).build();
    }
}
