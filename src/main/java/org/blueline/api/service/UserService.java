package org.blueline.api.service;

import lombok.RequiredArgsConstructor;
import org.blueline.api.exception.ConflictException;
import org.blueline.api.exception.UnauthorizedException;
import org.blueline.api.model.User;
import org.blueline.api.model.dto.UserDto;
import org.blueline.api.repository.UserRepository;
import org.hashids.Hashids;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final ActivityLogService activityLogService;

    public UserDto register(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ConflictException("Email already exists");
        } else {
            User user = modelMapper.map(userDto, User.class);
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            modelMapper.map(userRepository.save(user), UserDto.class);
            user.setFriendId(generateUniqueFriendCode(user.getId()));
            activityLogService.addRegisterActivity(user);
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
        user.setGender(userDto.getGender());
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

    public static String generateUniqueFriendCode(long number) {
        Hashids hashids = new Hashids("BlueLine-2025@secureSeed#v1", 5, "0123456789abcdefghijkmnpqrstuvwxyz");
        return hashids.encode(number);
    }

    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found with id " + userId));
        return modelMapper.map(user, UserDto.class);
    }


    public List<UserDto> getAll(Authentication authentication) {
        User user = authService.authenticate(authentication);

        if(!user.isAdmin()) {
            throw new UnauthorizedException("You do not have permission get all users");
        }
        return userRepository.findAll().stream()
                .map(u -> modelMapper.map(u, UserDto.class))
                .toList();
    }

}

