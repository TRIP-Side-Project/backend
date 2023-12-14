package com.api.trip.common.security.oauth;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
@Builder(access = AccessLevel.PRIVATE)
@SuppressWarnings("unchecked")
public class OAuth2Attribute {

    private Map<String, Object> attributes; // 소셜 로그인 사용자의 속성 정보를 담는 Map
    private String userNameAttributeName; // 사용자 속성의 키 값
    private String email; // 이메일
    private String nickname; // 이름
    private String profileImg; // 프로필 사진
    private String oauthType; // 제공자 정보

    static OAuth2Attribute of(String oauthType, String userNameAttributeName, Map<String, Object> attributes) {
        return switch (oauthType) {
            case "kakao" -> kakao(oauthType, userNameAttributeName, attributes);
            case "google" -> google(oauthType, userNameAttributeName, attributes);
            case "naver" -> naver(oauthType, userNameAttributeName, attributes);
            default -> throw new RuntimeException();
        };
    }


    private static OAuth2Attribute kakao(String oauthType, String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2Attribute.builder()
                .userNameAttributeName(userNameAttributeName)
                .attributes(kakaoAccount)
                .email((String) kakaoAccount.get("email"))
                .nickname((String) kakaoProfile.get("nickname"))
                .profileImg((String) kakaoProfile.get("profile_image_url"))
                .oauthType(oauthType)
                .build();
    }

    private static OAuth2Attribute google(String oauthType, String userNameAttributeName, Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
                .userNameAttributeName(userNameAttributeName)
                .attributes(attributes)
                .email((String) attributes.get("email"))
                .oauthType(oauthType)
                .build();
    }

    private static OAuth2Attribute naver(String oauthType, String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2Attribute.builder()
                .email((String) response.get("email"))
                .attributes(response)
                .oauthType(oauthType)
                .userNameAttributeName(userNameAttributeName)
                .build();
    }


    // OAuth2Attribute -> Map<String, Object>
    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("id", userNameAttributeName);
        map.put("email", email);
        map.put("nickname", nickname);
        map.put("profileImg", profileImg);
        map.put("oauthType", oauthType);

        return map;
    }
}
