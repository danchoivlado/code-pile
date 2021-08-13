package com.example.codepile.services;

import com.example.codepile.data.models.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    void registerUser(UserServiceModel userServiceModel);
    List<UserServiceModel> getAllUsers();
    void setAuthorityUserToUser(String userId);
}
