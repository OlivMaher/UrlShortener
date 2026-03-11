package com.olivmaher.urlshortener.repository;

import com.olivmaher.urlshortener.entity.Click;
import com.olivmaher.urlshortener.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClickRepository extends JpaRepository<Click, Long> {

    Long countByUrl(Url url);
}
