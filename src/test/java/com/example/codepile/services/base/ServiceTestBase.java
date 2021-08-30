package com.example.codepile.services.base;

import com.example.codepile.base.TestBase;
import com.example.codepile.data.entities.User;
import com.example.codepile.data.enums.Authority;
import com.example.codepile.data.models.service.user.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class ServiceTestBase extends TestBase {
    private final String userEmail = "email@email.com";
    private final String userPassword = "asdasd";
    private final String userUsername = "user";
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    protected PasswordEncoder getPasswordEncoder(){
        return this.passwordEncoder;
    }

    protected User getMockNormalUser(){
        User user = new User();
        user.setAuthority(Authority.USER);
        user.setEmail(userEmail);
        user.setPassword(passwordEncoder.encode(userPassword));
        user.setActive(true);
        user.setUsername(userUsername);
        return user;
    }

    protected UserDetails getMockedUserDetails(){
        return new org.springframework.security.core.userdetails
                .User(userUsername,
                passwordEncoder.encode(userPassword),
                Authority.USER.getGrantedAuthorities());
    }

    protected UserServiceModel getMockedUserServiceModelForRegistration(){
        UserServiceModel userServiceModel = new UserServiceModel();
        userServiceModel.setUsername(userUsername);
        userServiceModel.setEmail(userEmail);
        userServiceModel.setPassword(userPassword);
        return userServiceModel;
    }

    protected List<User> getMockedUsersListWithSize(Integer size){
        List<User> users = new ArrayList<User>();
        for (int i = 0; i < size; i++){
            users.add(new User());
        }

        return users;
    }


}
