package org.blueline.api.service;

import lombok.RequiredArgsConstructor;
import org.blueline.api.exception.ConflictException;
import org.blueline.api.model.User;
import org.blueline.api.model.dto.UserDto;
import org.blueline.api.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDto register(String password, UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ConflictException("Email already exists");
        } else {
            User user = modelMapper.map(userDto, User.class);
            user.setPassword(passwordEncoder.encode(password));
            return modelMapper.map(userRepository.save(user), UserDto.class);
        }
    }

    public UserDto getMe(Authentication authentication) {
        return modelMapper.map(authService.authenticate(authentication), UserDto.class);
    }

    public UserDto updateMe(Authentication authentication, UserDto userDto) {
        User user = authService.authenticate(authentication);
    
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setSex(userDto.getSex());
        user.setAvatar(userDto.getAvatar());
        user.setStatus(userDto.getStatus());
    
        return modelMapper.map(userRepository.save(user), UserDto.class);
    }
    
    
    public UserDto updatePassword(String oldPassword, String newPassword, Authentication authentication) {
        User user = authService.authenticate(authentication);

        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            return modelMapper.map(userRepository.save(user), UserDto.class);
        } else {
            throw new ConflictException("Invalid password");
        }
    }

    public void deleteMe(Authentication authentication) {
        User user = authService.authenticate(authentication);
        userRepository.delete(user);
    }
}

