package com.example.codepile.data.validation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class MessageCodes {

    public static final String INVALID_USERNAME_LENGTH_MESSAGE = "Username must be between 3 and 15 characters long.";
    public static final String NULL_USERNAME_MESSAGE = "Username cannot be null.";
    public static final String EMPTY_USERNAME_MESSAGE = "Username cannot be empty.";

    public static final String INVALID_PASSWORD_LENGTH_MESSAGE = "Password must be between 4 and 20 symbols long.";
    public static final String NULL_PASSWORD_MESSAGE = "Password cannot be null.";
    public static final String EMPTY_PASSWORD_MESSAGE = "Password cannot be empty.";

    public static final String EMAIL_PATTERN_STRING ="^((\"[\\w-\\s]+\")|([\\w-]+(?:\\.[\\w-]+)*)|(\"[\\w-\\s]+\")([\\w-]+(?:\\.[\\w-]+)*))(@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$)|(@\\[?((25[0-5]\\.|2[0-4][0-9]\\.|1[0-9]{2}\\.|[0-9]{1,2}\\.))((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\.){2}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\]?$)";
    public static final String INVALID_EMAIL_MESSAGE = "Invalid email.";
    public static final String NULL_EMAIL_MESSAGE = "Email cannot be null.";
    public static final String EMPTY_EMAIL_MESSAGE = "Email cannot be empty.";

    public static final String DUPLICATE_USERNAME = "Username already exist!";
    public static final String DUPLICATE_EMAIL = "Email already exist!";
    public static final String USER_USERNAME_NOTFOUND = "User with this username doesn't exists";
    public static final String USER_ID_NOTFOUND = "User with this ID doesn't exists";
    public static final String PASSWORD_DONT_MATCH = "Password is incorrect";

    public static final String PILE_ID_NOTFOUND = "Pile with this id doesn't exists";

}
