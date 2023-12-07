package com.api.trip.domain.email.repository;

import com.api.trip.domain.email.model.EmailAuth;
import com.api.trip.domain.email.model.QEmailAuth;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.Optional;

public class EmailAuthRepositoryCustomImpl implements EmailAuthRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public EmailAuthRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<EmailAuth> findValidAuthByEmail(String email, String authToken, LocalDateTime now) {

        EmailAuth emailAuth = jpaQueryFactory
                .selectFrom(QEmailAuth.emailAuth)
                .where(QEmailAuth.emailAuth.email.eq(email),
                        QEmailAuth.emailAuth.authToken.eq(authToken),
                        QEmailAuth.emailAuth.expireDate.goe(now),
                        QEmailAuth.emailAuth.expired.eq(false))
                .fetchFirst();

        return Optional.ofNullable(emailAuth);
    }
}
