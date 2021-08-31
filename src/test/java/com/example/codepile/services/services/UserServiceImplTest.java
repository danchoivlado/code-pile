package com.example.codepile.services.services;

import com.example.codepile.data.entities.User;
import com.example.codepile.data.enums.Authority;
import com.example.codepile.data.models.binding.user.EditProfieBindingModel;
import com.example.codepile.data.models.service.user.ChangeUserAuthorityServiceModel;
import com.example.codepile.data.models.service.user.NewPasswordServiceModel;
import com.example.codepile.data.models.service.user.ProfileServiceModel;
import com.example.codepile.data.models.service.user.UserServiceModel;
import com.example.codepile.data.repositories.UserRepository;
import com.example.codepile.error.authority.AuthorityNotFoundException;
import com.example.codepile.error.user.EmailAlreadyExistsException;
import com.example.codepile.error.user.PasswordDontMatchException;
import com.example.codepile.error.user.UsernameAlreadyExistsException;
import com.example.codepile.services.UserService;
import com.example.codepile.services.base.ServiceTestBase;
import org.hibernate.dialect.Sybase11Dialect;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static com.example.codepile.data.validation.MessageCodes.PASSWORD_DONT_MATCH;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest extends ServiceTestBase {

    @MockBean
    UserRepository userRepository;

    @Autowired
    UserService service;

    @Test
    void loadUserByUsername_WhenUserExistsWithUserName_ShouldReturnUserDetails() {
        String searchedUserWithUsername = "user";
        User user = getMockNormalUser();

        when(userRepository.existsUserByUsername(searchedUserWithUsername))
                .thenReturn(true);
        when(userRepository.findUserByUsername(searchedUserWithUsername))
                .thenReturn(user);

        UserDetails userDetails = service.loadUserByUsername(searchedUserWithUsername);

        assertEquals(userDetails.getUsername(),user.getUsername());
        assertEquals(userDetails.getPassword(),user.getPassword());
        assertEquals(userDetails.getAuthorities(),user.getAuthority().getGrantedAuthorities());
    }

    @Test
    void loadUserByUsername_WhenUserDoesNOTExistsWithUserName_ShouldThrowUsernameNotFoundException() {
        String serchedUserWithUsername = "user";

        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(serchedUserWithUsername));
    }

    @Test
    void registerUser_WithNONExistingUserNameAndEmail_ShouldRegisterUserWithUserAuthority() {
        UserServiceModel userServiceModel = super.getMockedUserServiceModelForRegistration();
        Integer usersCount = 2;
        List<User> userList = super.getMockedUsersListWithSize(usersCount);

        when(userRepository.existsUserByUsername(userServiceModel.getUsername()))
                .thenReturn(false);
        when(userRepository.existsUserByEmail(userServiceModel.getEmail()))
                .thenReturn(false);
        when(this.userRepository.findAll())
                .thenReturn(userList);

        service.registerUser(userServiceModel);

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argument.capture());

        User savedUser = argument.getValue();

        assertEquals(savedUser.getAuthority(), Authority.USER);
    }

    @Test
    void registerUser_WithNONExistingUserNameAndEmail_ShouldRegisterUserWithRootAuthority() {
        UserServiceModel userServiceModel = super.getMockedUserServiceModelForRegistration();
        Integer usersCount = 0;
        List<User> userList = super.getMockedUsersListWithSize(usersCount);

        when(userRepository.existsUserByUsername(userServiceModel.getUsername()))
                .thenReturn(false);
        when(userRepository.existsUserByEmail(userServiceModel.getEmail()))
                .thenReturn(false);
        when(this.userRepository.findAll())
                .thenReturn(userList);

        service.registerUser(userServiceModel);

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argument.capture());

        User savedUser = argument.getValue();

        assertEquals(savedUser.getAuthority(), Authority.ROOT);
    }

    @Test
    void registerUser_WithNONExistingUserNameAndEmail_ShouldRegisterUserProperCredentials() {
        UserServiceModel userServiceModel = super.getMockedUserServiceModelForRegistration();
        Integer usersCount = 2;
        List<User> userList = super.getMockedUsersListWithSize(usersCount);

        when(userRepository.existsUserByUsername(userServiceModel.getUsername()))
                .thenReturn(false);
        when(userRepository.existsUserByEmail(userServiceModel.getEmail()))
                .thenReturn(false);
        when(this.userRepository.findAll())
                .thenReturn(userList);

        service.registerUser(userServiceModel);

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argument.capture());

        User savedUser = argument.getValue();

        assertEquals(savedUser.getUsername(), userServiceModel.getUsername());
        assertEquals(savedUser.getEmail(), userServiceModel.getEmail());
    }

    @Test
    void registerUser_WithExistingUserNameAndNONExistingEmail_ShouldThrowUsernameAlreadyExistsException() {
        UserServiceModel userServiceModel = super.getMockedUserServiceModelForRegistration();
        Integer usersCount = 2;
        List<User> userList = super.getMockedUsersListWithSize(usersCount);

        when(userRepository.existsUserByUsername(userServiceModel.getUsername()))
                .thenReturn(true);
        when(userRepository.existsUserByEmail(userServiceModel.getEmail()))
                .thenReturn(false);

        assertThrows(UsernameAlreadyExistsException.class,()-> service.registerUser(userServiceModel));
    }

    @Test
    void registerUser_WithNONExistingUserNameAndExistingEmail_ShouldThrowEmailAlreadyExistsException() {
        UserServiceModel userServiceModel = super.getMockedUserServiceModelForRegistration();
        Integer usersCount = 2;
        List<User> userList = super.getMockedUsersListWithSize(usersCount);

        when(userRepository.existsUserByUsername(userServiceModel.getUsername()))
                .thenReturn(false);
        when(userRepository.existsUserByEmail(userServiceModel.getEmail()))
                .thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class,()-> service.registerUser(userServiceModel));
    }

    @Test
    void registerUser_WithExistingUserNameAndEmail_ShouldThrowUsernameAlreadyExistsException() {
        UserServiceModel userServiceModel = super.getMockedUserServiceModelForRegistration();
        Integer usersCount = 2;
        List<User> userList = super.getMockedUsersListWithSize(usersCount);

        when(userRepository.existsUserByUsername(userServiceModel.getUsername()))
                .thenReturn(true);
        when(userRepository.existsUserByEmail(userServiceModel.getEmail()))
                .thenReturn(true);

        assertThrows(UsernameAlreadyExistsException.class,()-> service.registerUser(userServiceModel));
    }

    @Test
    void getAllUsers_WithOneUser_ReturnProperMappedUser() {
        User user = new User();
        user.setUsername("user");
        String encodedPassword = super.getPasswordEncoder().encode("asdasd");
        user.setPassword(encodedPassword);
        user.setEmail("user@user.com");
        user.setAuthority(Authority.USER);

        List<User> userList = Arrays.asList(user);

        when(userRepository.findAll())
                .thenReturn(userList);

        List<UserServiceModel> returnedUsers = service.getAllUsers();
        UserServiceModel userServiceModel = returnedUsers.stream().findFirst().orElse(null);

        assertEquals(user.getUsername(),userServiceModel.getUsername());
        assertEquals(encodedPassword,userServiceModel.getPassword());
        assertEquals(user.getEmail(),userServiceModel.getEmail());
        assertEquals(user.getAuthority(),userServiceModel.getAuthority());
    }

    @Test
    void changeAuthorityUser_WithExistingUserAndExistingAuthorityWithUserAuthority_ShouldChangeTargetedUserAuthorityToAdmin() {
        ChangeUserAuthorityServiceModel serviceModel = new ChangeUserAuthorityServiceModel();
        serviceModel.setId("0");
        serviceModel.setToRole(Authority.ADMIN.getId());

        User targetedUser = new User();
        targetedUser.setAuthority(Authority.USER);

        when(userRepository.findUserById("0"))
                .thenReturn(targetedUser);
        when(userRepository.existsById("0"))
                .thenReturn(true);

        service.changeAuthorityUser(serviceModel);

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argument.capture());

        User savedUser = argument.getValue();

        assertEquals(savedUser.getAuthority(), Authority.ADMIN);
    }

    @Test
    void changeAuthorityUser_WithNONExistingUserAndExistingAuthorityWithUserAuthority_ShouldThrowUsernameNotFoundException() {
        ChangeUserAuthorityServiceModel serviceModel = new ChangeUserAuthorityServiceModel();
        serviceModel.setId("0");
        serviceModel.setToRole(Authority.ADMIN.getId());

        User targetedUser = new User();
        targetedUser.setAuthority(Authority.USER);

        when(userRepository.findUserById("0"))
                .thenReturn(targetedUser);
        when(userRepository.existsById("0"))
                .thenReturn(false);

        assertThrows(UsernameNotFoundException.class,() -> service.changeAuthorityUser(serviceModel));
    }

    @Test
    void changeAuthorityUser_WithExistingUserAndNONExistingAuthorityWithUserAuthority_ShouldThrowAuthorityNotFoundException() {
        ChangeUserAuthorityServiceModel serviceModel = new ChangeUserAuthorityServiceModel();
        serviceModel.setId("0");
        serviceModel.setToRole("non-existence-authority");

        User targetedUser = new User();
        targetedUser.setAuthority(Authority.USER);

        when(userRepository.findUserById("0"))
                .thenReturn(targetedUser);
        when(userRepository.existsById("0"))
                .thenReturn(true);

        assertThrows(AuthorityNotFoundException.class,() -> service.changeAuthorityUser(serviceModel));
    }

    @Test
    void getProfile_WhenUserWithUserNameDoesNOTExists_ShouldThrowUsernameNotFoundException() {
        String searchedUserName = "user";

        when(userRepository.existsUserByUsername(searchedUserName))
                .thenReturn(false);

        assertThrows(UsernameNotFoundException.class, () -> service.getProfile(searchedUserName));
    }

    @Test
    void getProfile_WhenUserWithUserNameDoesExists_ShouldGetProperMappedProfileServiceModel() {
        User user = new User();
        user.setUsername("user");
        user.setEmail("user@user.com");

        String searchedUserName = "user";

        when(userRepository.existsUserByUsername(searchedUserName))
                .thenReturn(true);
        when(userRepository.findUserByUsername(searchedUserName))
                .thenReturn(user);

        ProfileServiceModel serviceModel = service.getProfile(searchedUserName);

        assertEquals(user.getUsername(),serviceModel.getUsername());
        assertEquals(user.getEmail(),serviceModel.getEmail());
    }


    @Test
    void getEditProfile_WhenUserDoesNOTExist_ShouldThrowUsernameNotFoundException() {
        String searchedUserUserName = "user";

        when(userRepository.existsUserByUsername(searchedUserUserName))
                .thenReturn(false);

        assertThrows(UsernameNotFoundException.class, ()-> this.service.getEditProfile(searchedUserUserName));
    }

    @Test
    void getEditProfile_WhenUserExist_ShouldReturnProperMappedEditProfieBindingModel() {
        User user = new User();
        user.setUsername("user");
        user.setEmail("user@user.com");
        String encodedPassword = super.getPasswordEncoder().encode("asdasd");
        user.setPassword(encodedPassword);
        
        String searchedUserUserName = "user";

        when(userRepository.existsUserByUsername(searchedUserUserName))
                .thenReturn(true);
        when(userRepository.findUserByUsername(searchedUserUserName))
                .thenReturn(user);

        EditProfieBindingModel editProfieBindingModel  = this.service.getEditProfile(searchedUserUserName);

        assertEquals(user.getUsername(), editProfieBindingModel.getUsername());
        assertEquals(user.getEmail(), editProfieBindingModel.getEmail());
        assertEquals(encodedPassword, editProfieBindingModel.getPassword());
    }

    @Test
    void changePassword_WhenUserWithUserNameDoesNOTExists_ShouldThrowUsernameNotFoundException() {
        NewPasswordServiceModel serviceModel = new NewPasswordServiceModel();
        String searchedUserUserName = "user";

        when(userRepository.existsUserByUsername(searchedUserUserName))
                .thenReturn(false);

        assertThrows(UsernameNotFoundException.class, () -> service.changePassword(serviceModel));
    }

    @Test
    void changePassword_WhenPasswordInputPasswordAndOriginalPasswordDoesNOTMatch_ShouldChangePassword() {
        String inputOldPassword = "asdasd";
        String inputNewPassword = "1234";
        String inputConfirmNewPassword = inputNewPassword;
        String originalUserPassword = inputOldPassword;
        String encryptedUserPassword = super.getPasswordEncoder().encode(originalUserPassword);
        String searchedUserUserName = "user";


        User user =  new User();
        user.setUsername(searchedUserUserName);
        user.setPassword(encryptedUserPassword);

        NewPasswordServiceModel serviceModel = new NewPasswordServiceModel();
        serviceModel.setUsername(searchedUserUserName);
        serviceModel.setOldPassword(inputOldPassword);
        serviceModel.setPassword(inputNewPassword);
        serviceModel.setConfirmPassword(inputConfirmNewPassword);

        when(userRepository.existsUserByUsername(searchedUserUserName))
                .thenReturn(true);
        when(userRepository.findUserByUsername(searchedUserUserName))
                .thenReturn(user);

        this.service.changePassword(serviceModel);

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argument.capture());

        User savedUser = argument.getValue();


        assertTrue(super.getPasswordEncoder().matches(inputNewPassword, savedUser.getPassword()));
    }

    @Test
    void changePassword_WhenPasswordInputPasswordAndOriginalPasswordDoesNOTMatch_ShouldThrowPasswordDontMatchException() {
        String inputOldPassword = "asdasd";
        String originalUserPassword = inputOldPassword + "not Matched Password";
        String encryptedUserPassword = super.getPasswordEncoder().encode(originalUserPassword);
        String searchedUserUserName = "user";


        User user =  new User();
        user.setUsername(searchedUserUserName);
        user.setPassword(encryptedUserPassword);

        NewPasswordServiceModel serviceModel = new NewPasswordServiceModel();
        serviceModel.setUsername(searchedUserUserName);
        serviceModel.setOldPassword(inputOldPassword);

        when(userRepository.existsUserByUsername(searchedUserUserName))
                .thenReturn(true);
        when(userRepository.findUserByUsername(searchedUserUserName))
                .thenReturn(user);

        assertThrows(PasswordDontMatchException.class, ()-> this.service.changePassword(serviceModel));
    }


    @Test
    void checkUserExistsWithUsername_WhenUserExists_ShouldReturnTrue() {
        String searchedUserUserName = "user";

        when(userRepository.existsUserByUsername(searchedUserUserName))
                .thenReturn(true);

        assertTrue(this.service.checkUserExistsWithUsername(searchedUserUserName));
    }

    @Test
    void checkUserExistsWithUsername_WhenUserDoesNOTExists_ShouldReturnFalse() {
        String searchedUserUserName = "user";

        when(userRepository.existsUserByUsername(searchedUserUserName))
                .thenReturn(false);

        assertFalse(this.service.checkUserExistsWithUsername(searchedUserUserName));
    }


    @Test
    void getUserId_WhenUserDoesNOTExists_ShouldThrowUsernameNotFoundException() {
        String searchedUserUserName = "user";

        when(userRepository.existsUserByUsername(searchedUserUserName))
                .thenReturn(false);

        assertThrows(UsernameNotFoundException.class, ()-> this.service.getUserId(searchedUserUserName));
    }

    @Test
    void getUserId_WhenUserExists_ShouldReturnProperId() {
        String searchedUserUserName = "user";
        String userId = "1";


        User user =  new User();
        user.setUsername(searchedUserUserName);
        user.setId(userId);

        when(userRepository.existsUserByUsername(searchedUserUserName))
                .thenReturn(true);
        when(userRepository.findUserByUsername(searchedUserUserName))
                .thenReturn(user);

        String returnedUserId = this.service.getUserId(searchedUserUserName);

        assertEquals(returnedUserId, userId);
    }
}