package com.api.trip.domain.email.repository;

import com.api.trip.domain.email.model.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long>, EmailAuthRepositoryCustom {

    Optional<EmailAuth> findByEmail(String email);

}
