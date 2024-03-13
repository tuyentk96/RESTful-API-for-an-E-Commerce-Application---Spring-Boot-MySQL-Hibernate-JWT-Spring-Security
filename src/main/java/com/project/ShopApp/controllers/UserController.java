package com.project.ShopApp.controllers;

import com.project.ShopApp.dto.request.UserRegisterRequest;
import com.project.ShopApp.dto.request.UserLoginRequest;
import com.project.ShopApp.dto.respone.ValidatedErrorResponse;
import com.project.ShopApp.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest,
                                        BindingResult result){
        if (result.hasErrors()){
            return ValidatedErrorResponse.validatedErrorResponse(result);
        }

        return ResponseEntity.ok(userService.createUser(userRegisterRequest));
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest userLoginRequest,
                                   BindingResult result){
        if (result.hasErrors()){
            return ValidatedErrorResponse.validatedErrorResponse(result);
        }

        return ResponseEntity.ok(userService.login(userLoginRequest.getPhoneNumber(),userLoginRequest.getPassword()));
    }
}
