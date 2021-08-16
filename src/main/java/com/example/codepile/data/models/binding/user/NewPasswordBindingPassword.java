package com.example.codepile.data.models.binding.user;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static com.example.codepile.data.validation.MessageCodes.*;

@Getter
@Setter
public class NewPasswordBindingPassword {
    @NotNull(message = NULL_PASSWORD_MESSAGE)
    @NotEmpty(message = EMPTY_PASSWORD_MESSAGE)
    @Length(min = 4, max = 20, message = INVALID_PASSWORD_LENGTH_MESSAGE)
    private String oldPassword;

    @NotNull(message = NULL_PASSWORD_MESSAGE)
    @NotEmpty(message = EMPTY_PASSWORD_MESSAGE)
    @Length(min = 4, max = 20, message = INVALID_PASSWORD_LENGTH_MESSAGE)
    private String password;

    @NotNull(message = NULL_PASSWORD_MESSAGE)
    @NotEmpty(message = EMPTY_PASSWORD_MESSAGE)
    @Length(min = 4, max = 20, message = INVALID_PASSWORD_LENGTH_MESSAGE)
    private String confirmPassword;
}
