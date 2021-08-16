package com.example.codepile.data.models.binding.user;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.example.codepile.data.validation.MessageCodes.*;

@Getter
@Setter
public class UserRegisterBindingModel {

    @NotNull(message = NULL_USERNAME_MESSAGE)
    @NotEmpty(message = EMPTY_USERNAME_MESSAGE)
    @Length(min = 3, max = 15, message = INVALID_USERNAME_LENGTH_MESSAGE)
    private String username;

    @NotNull(message = NULL_PASSWORD_MESSAGE)
    @NotEmpty(message = EMPTY_PASSWORD_MESSAGE)
    @Length(min = 4, max = 20, message = INVALID_PASSWORD_LENGTH_MESSAGE)
    private String password;

    @NotNull(message = NULL_PASSWORD_MESSAGE)
    @NotEmpty(message = EMPTY_PASSWORD_MESSAGE)
    @Length(min = 4, max = 20, message = INVALID_PASSWORD_LENGTH_MESSAGE)
    private String confirmPassword;

    @NotNull(message = NULL_EMAIL_MESSAGE)
    @NotEmpty(message = EMPTY_EMAIL_MESSAGE)
    @Pattern(regexp = EMAIL_PATTERN_STRING, message = INVALID_EMAIL_MESSAGE)
    private String email;
}
