package com.api.trip.domain.email.repository;

import com.api.trip.domain.email.model.EmailAuth;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailAuthRepositoryCustom {
    Optional<EmailAuth> findValidAuthByEmail(String email, String authToken, LocalDateTime now);

}
