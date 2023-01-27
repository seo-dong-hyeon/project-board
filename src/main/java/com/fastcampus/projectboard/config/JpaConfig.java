package com.fastcampus.projectboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {
    // createdBy에 들어갈 생성자
    @Bean
    public AuditorAware<String> auditorAware(){
        return () -> Optional.of("dhseo"); // TODO: 스프링 시큐리티로 인증 기능을 붙일 때 수정
    }
}
