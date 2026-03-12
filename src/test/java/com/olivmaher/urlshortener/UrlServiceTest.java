package com.olivmaher.urlshortener;

import com.olivmaher.urlshortener.entity.Url;
import com.olivmaher.urlshortener.entity.User;
import com.olivmaher.urlshortener.exception.ResourceNotFoundException;
import com.olivmaher.urlshortener.exception.UnauthorizedException;
import com.olivmaher.urlshortener.repository.ClickRepository;
import com.olivmaher.urlshortener.repository.UrlRepository;
import com.olivmaher.urlshortener.repository.UserRepository;
import com.olivmaher.urlshortener.service.UrlService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ClickRepository clickRepository;

    @InjectMocks
    private UrlService urlService;

    @Test
    void deleteUrl_shouldThrow_whenUserDoesNotOwnUrl() {
        User owner = new User("owner@email.com", "password");
        User otherUser = new User("other@email.com", "password");

        Url url = new Url("https://google.com", owner);

        when(urlRepository.findById(1L)).thenReturn(Optional.of(url));
        when(userRepository.findByEmail("other@email.com"))
                .thenReturn(Optional.of(otherUser));

        assertThrows(UnauthorizedException.class, () -> {
            urlService.deleteUrl(1L, "other@email.com");
        });
    }

    @Test
    void deleteUrl_shouldDeactivateUrl_whenUserOwnsUrl() {
        User owner = new User("owner@email.com", "password");
        Url url = new Url("https://google.com", owner);

        when(urlRepository.findById(1L)).thenReturn(Optional.of(url));
        when(userRepository.findByEmail("owner@email.com"))
                .thenReturn(Optional.of(owner));

        urlService.deleteUrl(1L, "owner@email.com");

        assertFalse(url.isActive());
        verify(urlRepository).save(url);
    }

    @Test
    void getOriginalUrl_shouldThrow_whenUrlIsNotActive() {
        User owner = new User("owner@email.com", "password");
        Url url = new Url("https://google.com", owner);
        url.setActive(false);

        when(urlRepository.findByShortCode("abc"))
                .thenReturn(Optional.of(url));

        assertThrows(ResourceNotFoundException.class, () -> {
            urlService.getOriginalUrl("abc");
        });
    }
}