package com.example.codepile.services;

import com.example.codepile.data.models.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void registerUser(UserServiceModel userServiceModel);
}
