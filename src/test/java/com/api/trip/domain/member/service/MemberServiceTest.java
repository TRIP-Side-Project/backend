package com.api.trip.domain.member.service;

import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 회원")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Spy
    private PasswordEncoder passwordEncoder;

    @DisplayName("현재 비밀번호를 입력하면 회원 탈퇴가 완료된다.")
    @Test
    void successDeleteMember() {

        // Given
        String email = "test@email.com";
        String password = "1234";

        Member member = Member.builder()
                .password(passwordEncoder.encode(password))
                .build();

        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(mock(Member.class)));
        when(passwordEncoder.matches(password, member.getPassword())).thenReturn(true);
        doNothing().when(memberRepository).deleteById(anyLong());

        // When
        memberService.deleteMember(email, password);

        // Then
        then(memberRepository).should().findByEmail(anyString());
        then(passwordEncoder).should().matches(password, member.getPassword());
        then(memberRepository).should().deleteById(anyLong());
    }

}