package com.example.codepile.services;

import com.example.codepile.data.models.binding.user.EditProfieBindingModel;
import com.example.codepile.data.models.service.user.*;
import org.modelmapper.spi.StrongTypeConditionalConverter;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    void registerUser(UserServiceModel userServiceModel);
    List<UserServiceModel> getAllUsers();
    void changeAuthorityUser(ChangeUserAuthorityServiceModel model);
    ProfileServiceModel getProfile(String username);
    EditProfieBindingModel getEditProfile(String username);
    void editProfile(EditProfileServiceModel profileServiceModel);
    void changePassword(NewPasswordServiceModel serviceModel);
    boolean checkUserExistsWithUsername(String username);
    String getUserId(String username);
}
