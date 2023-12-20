package com.api.trip.domain.email.service;

import com.api.trip.common.exception.ErrorCode;
import com.api.trip.common.exception.custom_exception.BadRequestException;
import com.api.trip.common.exception.custom_exception.NotFoundException;
import com.api.trip.domain.email.model.EmailAuth;
import com.api.trip.domain.email.repository.EmailAuthRepository;
import com.api.trip.domain.member.controller.dto.EmailResponse;
import com.api.trip.domain.member.controller.dto.FindPasswordRequest;
import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.member.repository.MemberRepository;
import com.api.trip.domain.member.service.MemberService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@EnableAsync
@Transactional
@RequiredArgsConstructor
public class EmailService {

    private final MemberService memberService;
    private final JavaMailSender javaMailSender;
    private final EmailAuthRepository emailAuthRepository;
    private final MemberRepository memberRepository;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void send(String email, String authToken) {

        if (memberRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("이메일 인증이 완료된 회원입니다!");
        }

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            Context context = new Context();
            context.setVariable("auth_url", "https://triptrip.site/api/members/auth-email/%s/%s".formatted(email, authToken));

            String html = templateEngine.process("email_auth_mail", context);

            message.setSubject("[TRIP TRIP] 이메일 인증 안내입니다.");
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setText(html, "UTF-8", "HTML");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        javaMailSender.send(message);
    }

    @Async
    public void sendNewPassword(FindPasswordRequest findPasswordRequest) {
        String email = findPasswordRequest.getEmail();

        if (email == null || email.isEmpty()) {
            throw new BadRequestException(ErrorCode.EMPTY_EMAIL);
        }

        // 가입 회원 여부 검사
        Member member = memberService.getMemberByEmail(email);
        String newPassword = getRandomPassword();

        MimeMessage message = javaMailSender.createMimeMessage();
        try {

            Context context = new Context();
            context.setVariable("newPassword", newPassword);

            String html = templateEngine.process("find_password_mail", context);

            message.setSubject("[TRIP TRIP] 임시 비밀번호 안내입니다.");
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setText(html, "UTF-8", "HTML");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        javaMailSender.send(message);
        memberService.changePassword(email, newPassword); // 새 비밀번호로 업데이트
    }

    // 인증 메일 검증
    public EmailResponse authEmail(String email, String authToken) {
        EmailAuth emailAuth = emailAuthRepository.findValidAuthByEmail(email, authToken, LocalDateTime.now())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_EMAIL_TOKEN));

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

    // TODO: 랜덤 비밀번호 생성 -> 임시로 8자리 랜덤 문자열 반환
    private String getRandomPassword() {
        char[] charSet = new char[] {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
        };

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        int rndAllCharactersLength = charSet.length;
        for (int i = 0; i < 8; i++) {
            sb.append(charSet[random.nextInt(rndAllCharactersLength)]);
        }

        return sb.toString();
    }

}
