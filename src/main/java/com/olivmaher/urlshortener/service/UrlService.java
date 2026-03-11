package com.olivmaher.urlshortener.service;

import com.olivmaher.urlshortener.entity.Url;
import com.olivmaher.urlshortener.entity.User;
import com.olivmaher.urlshortener.repository.UrlRepository;
import com.olivmaher.urlshortener.repository.UserRepository;
import com.olivmaher.urlshortener.util.Base62Encoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UrlService {

    private final UserRepository userRepository;
    private final UrlRepository urlRepository;


    public UrlService(UserRepository userRepository, UrlRepository urlRepository) {
        this.userRepository = userRepository;
        this.urlRepository = urlRepository;
    }

    public String shortenUrl(String originalUrl, String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Url url = new Url(originalUrl, user);

        urlRepository.save(url);
        String shortCode = Base62Encoder.encode(url.getId());
        url.setShortCode(shortCode);
        urlRepository.save(url);
        return shortCode;
    }

    public String getOriginalUrl(String shortCode){
        Url url = urlRepository.findByShortCode(shortCode).orElseThrow(() -> new RuntimeException("Url not found"));
        if(!url.isActive()){
            throw new RuntimeException("Url not active");
        }
        return url.getOriginalUrl();
    }

    public List<Url> getUserUrls(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return urlRepository.findAllByUser(user);
    }

    public void deleteUrl(Long id, String email){
        Url url = urlRepository.findById(id).orElseThrow(() -> new RuntimeException("Url not found"));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        if(!Objects.equals(url.getUser().getEmail(), user.getEmail())){
            throw new RuntimeException("Permission not granted");
        }
        url.setActive(false);
        urlRepository.save(url);
    }
}
