package com.example.codepile.data.models.service.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPasswordServiceModel {
    private String username;
    private String oldPassword;
    private String password;
    private String confirmPassword;
}
