package com.example.codepile.data.models.service.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterServiceModel {
    private String username;
    private String password;
    private String email;
}
