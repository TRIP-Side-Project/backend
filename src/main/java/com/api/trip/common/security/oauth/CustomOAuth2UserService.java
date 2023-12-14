package com.api.trip.common.security.oauth;

import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String oauthType = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(oauthType, userNameAttributeName, oAuth2User.getAttributes());
        Map<String, Object> customAttributeMap = oAuth2Attribute.convertToMap();

        String email = (String) customAttributeMap.get("email");
        Optional<Member> findMember = memberRepository.findByEmail(email);

        boolean exist = findMember.isPresent();
        String authority = exist ? findMember.get().getRole().getValue() : "ROLE_MEMBER";

        log.debug("회원 존재 여부: {}", exist);
        log.debug("회원 권한 여부: {}", authority);

        customAttributeMap.put("exist", exist);
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(authority)), customAttributeMap, userNameAttributeName);
    }
}
