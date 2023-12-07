package com.api.trip.domain.email.service;

import com.api.trip.domain.email.model.EmailAuth;
import com.api.trip.domain.email.repository.EmailAuthRepository;
import com.api.trip.domain.member.controller.dto.EmailResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@EnableAsync
@Transactional
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final EmailAuthRepository emailAuthRepository;

    @Async
    public void send(String email, String authToken) {
        MimeMessage message = javaMailSender.createMimeMessage();
        String text = "http://localhost:8080/api/members/auth-email/%s/%s" .formatted(email, authToken);

        try {
            message.setSubject("[Trip Trip] 회원가입 인증 메일");
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setText(text, "UTF-8", "HTML");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        javaMailSender.send(message);
    }

    // 인증 메일 검증
    public EmailResponse authEmail(String email, String authToken) {
        EmailAuth emailAuth = emailAuthRepository.findValidAuthByEmail(email, authToken, LocalDateTime.now())
                .orElseThrow(() -> new RuntimeException("토큰 정보가 일치하지 않습니다!"));

        emailAuth.useToken(); // 토큰 사용 -> 만료
        return EmailResponse.of(emailAuth.isExpired());
    }

    public String createEmailAuth(String email) {
        EmailAuth emailAuth = EmailAuth.builder()
                .email(email)
                .authToken(UUID.randomUUID().toString())
                .expired(false)
                .build();

        return emailAuthRepository.save(emailAuth).getAuthToken();
    }

}
