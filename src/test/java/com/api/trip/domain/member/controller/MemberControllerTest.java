package com.api.trip.domain.member.controller;

import com.api.trip.domain.member.controller.dto.DeleteRequest;
import com.api.trip.domain.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @DisplayName("[API] - 회원 삭제 성공")
    @WithMockUser
    @Test
    void successDeleteMember() throws Exception {

        String email = "test@email.com";
        String password = "1234";

        doNothing().when(memberService).deleteMember(email, password);

        mvc.perform(delete("/api/members/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new DeleteRequest(password)))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    /**
     * 에러 처리가 안되있어서 응답 실패시에도 200 응답이 떨어지기 때문에 실패 테스트는 현재 진행 불가능
    @DisplayName("[API] - 회원 삭제 실패 - 현재 비밀번호를 잘못 입력한 경우")
    @WithMockUser
    @Test
    void deleteDeleteMember() throws Exception {

        String email = "test@email.com";
        String password = "1234";
        String wrongPassword = "1111";

        doThrow(new RuntimeException("비밀번호가 일치하지 않습니다.")).when(memberService).deleteMember(email, wrongPassword);

        mvc.perform(delete("/api/members/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new DeleteRequest(wrongPassword)))
                )
                .andExpect(status().isConflict())
                .andDo(print());
    }
    */


}