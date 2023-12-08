package com.api.trip.common.auditing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class AuditingConfig {

    // TODO: 회원가입 후 유저 정보를 가져와서 넣어주는 방법을 모르겠습니다...
    /**
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("user1");
    }
    */
}
