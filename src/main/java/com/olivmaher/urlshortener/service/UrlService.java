package com.olivmaher.urlshortener.service;

import com.olivmaher.urlshortener.dto.AnalyticsResponse;
import com.olivmaher.urlshortener.dto.UrlResponse;
import com.olivmaher.urlshortener.entity.Click;
import com.olivmaher.urlshortener.entity.Url;
import com.olivmaher.urlshortener.entity.User;
import com.olivmaher.urlshortener.exception.ResourceNotFoundException;
import com.olivmaher.urlshortener.exception.UnauthorizedException;
import com.olivmaher.urlshortener.repository.ClickRepository;
import com.olivmaher.urlshortener.repository.UrlRepository;
import com.olivmaher.urlshortener.repository.UserRepository;
import com.olivmaher.urlshortener.util.Base62Encoder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UrlService {

    private final UserRepository userRepository;
    private final UrlRepository urlRepository;
    private final ClickRepository clickRepository;

    public UrlService(UserRepository userRepository, UrlRepository urlRepository, ClickRepository clickRepository) {
        this.userRepository = userRepository;
        this.urlRepository = urlRepository;
        this.clickRepository = clickRepository;
    }

    public String shortenUrl(String originalUrl, String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Url url = new Url(originalUrl, user);

        urlRepository.save(url);
        String shortCode = Base62Encoder.encode(url.getId());
        url.setShortCode(shortCode);
        urlRepository.save(url);
        return shortCode;
    }

    public String getOriginalUrl(String shortCode){
        Url url = urlRepository.findByShortCode(shortCode).orElseThrow(() -> new ResourceNotFoundException("Url not found"));
        if(!url.isActive()){
            throw new ResourceNotFoundException("Url not active");
        }
        Click click = new Click(url);
        clickRepository.save(click);
        return url.getOriginalUrl();
    }

    public List<UrlResponse> getUserUrls(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return urlRepository.findAllByUser(user).stream().map(UrlResponse::new).toList();
    }

    public void deleteUrl(Long id, String email){
        Url url = belongsToUser(id, email);
        url.setActive(false);
        urlRepository.save(url);
    }

    public AnalyticsResponse getAnalytics(Long id, String email){
        Url url = belongsToUser(id, email);
        long totalClicks = clickRepository.countByUrl(url);
        return new AnalyticsResponse(totalClicks, url.getShortCode(), url.getOriginalUrl());
    }

    private Url belongsToUser(Long id, String email){
        Url url = urlRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Url not found"));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if(!Objects.equals(url.getUser().getEmail(), user.getEmail())){
            throw new UnauthorizedException("Permission not granted");
        }
        return url;
    }
}
