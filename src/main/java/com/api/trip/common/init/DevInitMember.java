package com.api.trip.common.init;

import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.member.model.MemberRole;
import com.api.trip.domain.member.model.SocialCode;
import com.api.trip.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("dev")
public class DevInitMember {

    @Value("${cloud.aws.default-image}")
    private String defaultProfileImg;

    @Bean
    public CommandLineRunner init(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            memberRepository.save(createMember("trip123@test.com", "trip123", passwordEncoder.encode("trip1234")));
            memberRepository.save(createMember("admin@test.com", "admin", passwordEncoder.encode("trip1234")));
        };
    }

    private Member createMember(String email, String nickname, String password) {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .profileImg(defaultProfileImg)
                .socialCode(SocialCode.NORMAL)
                .build();
    }
}
