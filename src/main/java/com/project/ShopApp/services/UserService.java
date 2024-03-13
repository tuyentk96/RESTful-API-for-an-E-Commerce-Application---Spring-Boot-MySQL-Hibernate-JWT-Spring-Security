package com.project.ShopApp.services;

import com.project.ShopApp.dto.request.UserRegisterRequest;
import com.project.ShopApp.models.User;

public interface UserService {
    User createUser(UserRegisterRequest userRegisterRequest);
    String login(String phoneNumber,String password);
}
