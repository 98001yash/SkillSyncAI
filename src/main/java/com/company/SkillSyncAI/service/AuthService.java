package com.company.SkillSyncAI.service;


import com.company.SkillSyncAI.dtos.SignupDto;
import com.company.SkillSyncAI.dtos.UserDto;
import com.company.SkillSyncAI.entities.User;
import com.company.SkillSyncAI.enums.Role;
import com.company.SkillSyncAI.exceptions.ResourceNotFoundException;
import com.company.SkillSyncAI.exceptions.RuntimeConflictException;
import com.company.SkillSyncAI.repository.UserRepository;
import com.company.SkillSyncAI.security.JWTService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public String[] login(String email, String password){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email,password)
        );

        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new String[]{accessToken, refreshToken};
    }

    @Transactional
    public UserDto signup(SignupDto signUpDto){
        // Check if the user already exists
        if (userRepository.findByEmail(signUpDto.getEmail()).isPresent()) {
            throw new RuntimeConflictException("Cannot signup, User already exists with email " + signUpDto.getEmail());
        }

        // Map DTO to User entity
        User mappedUser = modelMapper.map(signUpDto, User.class);

        // Assign role based on request, default to LEARNER if not specified
        Role assignedRole = (signUpDto.getRole() != null && signUpDto.getRole().equalsIgnoreCase("MENTOR"))
                ? Role.MENTOR
                : Role.LEARNER;

        mappedUser.setRoles(Set.of(assignedRole));
        mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));

        // Save user
        User savedUser = userRepository.save(mappedUser);

        // TODO: Create user-related entities if needed
        return modelMapper.map(savedUser, UserDto.class);
    }


    public String refreshToken(String refreshToken){
        Long userId = jwtService.getUserIdFromToken(refreshToken);

        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found with id: "+userId));
        return jwtService.generateAccessToken(user);
    }
}

