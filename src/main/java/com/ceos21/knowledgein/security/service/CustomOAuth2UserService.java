package com.ceos21.knowledgein.security.service;

import com.ceos21.knowledgein.security.dto.OAuth2UserInfo;
import com.ceos21.knowledgein.security.dto.PrincipalUserDetails;
import com.ceos21.knowledgein.user.domain.UserEntity;
import com.ceos21.knowledgein.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        Map<String, Object> attributes = super.loadUser(userRequest).getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registrationId, attributes);
        UserEntity userEntity = getOrSave(oAuth2UserInfo);

        return new PrincipalUserDetails(userEntity, attributes);
    }

    private UserEntity getOrSave(OAuth2UserInfo oAuth2UserInfo) {
        UserEntity userEntity = userRepository.findByEmail(oAuth2UserInfo.email())
                .orElseGet(oAuth2UserInfo::toEntity);
        return userRepository.save(userEntity);
    }
}
