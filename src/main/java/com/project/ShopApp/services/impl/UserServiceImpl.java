package com.project.ShopApp.services.impl;

import com.project.ShopApp.component.JwtTokenUtil;
import com.project.ShopApp.dto.request.UserRegisterRequest;
import com.project.ShopApp.exception.ErrorResponse;
import com.project.ShopApp.exception.ErrorResult;
import com.project.ShopApp.models.Role;
import com.project.ShopApp.models.User;
import com.project.ShopApp.repositories.RoleRepository;
import com.project.ShopApp.repositories.UserRepository;
import com.project.ShopApp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    @Override
    public User createUser(UserRegisterRequest userRegisterRequest){
        String phoneNumber = userRegisterRequest.getPhoneNumber();
        if (userRepository.existsByPhoneNumber(phoneNumber)){
            throw new ErrorResponse(ErrorResult.PHONE_NUMBER_EXISTING);
        }
        if (!userRegisterRequest.getPassword().equals(userRegisterRequest.getRetypePassword())){
            throw new ErrorResponse(ErrorResult.PASSWORD_NOT_MATCH);
        }

        Role role = roleRepository.findById(userRegisterRequest.getRoleId())
                .orElseThrow(() -> new ErrorResponse(ErrorResult.NOT_FOUND_ROLE));

        User newUser = User.builder()
                .fullName(userRegisterRequest.getFullName())
                .phoneNumber(userRegisterRequest.getPhoneNumber())
                .address(userRegisterRequest.getAddress())
                .dateOfBirth(userRegisterRequest.getDateOfBirth())
                .password(userRegisterRequest.getPassword())
                .facebookAccountId(userRegisterRequest.getFacebookAccountId())
                .googleAccountId(userRegisterRequest.getGoogleAccountId())
                .build();

        newUser.setRole(role);
        if (userRegisterRequest.getFacebookAccountId() == 0 && userRegisterRequest.getGoogleAccountId() == 0){
            String password = userRegisterRequest.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) {
        User existingUser = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(()->new ErrorResponse(ErrorResult.INVALID_USER));
        if (existingUser.getFacebookAccountId() == 0 && existingUser.getGoogleAccountId() == 0){
            if (!passwordEncoder.matches(password,existingUser.getPassword())){
                throw new ErrorResponse(ErrorResult.INVALID_USER);
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(phoneNumber,password,existingUser.getAuthorities());
        authenticationManager.authenticate(authenticationToken);

        return jwtTokenUtil.generateToken(existingUser);
    }
}
