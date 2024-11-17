package org.blueline.api.service;

import lombok.RequiredArgsConstructor;
import org.blueline.api.exception.UnauthorizedException;
import org.blueline.api.model.User;
import org.blueline.api.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return jwtService.generateToken(user);
        }
        throw new UnauthorizedException("Invalid login credentials");
    }

    public User authenticate(Authentication authentication) {
        return userRepository.findById(jwtService.getUserIdFromToken(authentication.getPrincipal().toString())).orElseThrow(() -> new UnauthorizedException("Invalid token"));
    }
}