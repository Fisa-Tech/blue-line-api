package org.blueline.api.service;

import lombok.RequiredArgsConstructor;
import org.blueline.api.model.OAuth2Token;
import org.blueline.api.model.User;
import org.blueline.api.repository.TokenRepository;
import org.blueline.api.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String accessToken = userRequest.getAccessToken().getTokenValue();
        Instant tokenExpiry = userRequest.getAccessToken().getExpiresAt();

        // String email = oAuth2User.getAttribute("email");
        // User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
        String firstName = oAuth2User.getAttribute("firstname");
        String lastName = oAuth2User.getAttribute("lastname");

        User user = userRepository.findByFirstnameAndLastname(firstName, lastName).orElse(new User());
        user.setFirstname(firstName);
        user.setLastname(lastName);
        user.setEmail("test@gmail.com");

        OAuth2Token token = tokenRepository.findByUserAndProvider(user, provider).orElse(new OAuth2Token());

        token.setProvider(provider);
        token.setAccessToken(accessToken);
        token.setTokenExpiry(tokenExpiry);
        token.setUser(user);

        tokenRepository.save(token);

        return oAuth2User;
    }
}
