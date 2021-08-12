package com.example.codepile.data.models.service;

import com.example.codepile.data.enums.Authority;
import com.example.codepile.data.models.service.base.UIDBaseServiceModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserServiceModel extends UIDBaseServiceModel {
    private String username;
    private String password;
    private Authority authority;
    private String email;
}
