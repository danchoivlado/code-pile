package com.example.codepile.data.models.view;

import com.example.codepile.data.enums.Authority;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserViewModel {
    private String id;
    private String username;
    private String email;
    private List<String> authorities;
}
