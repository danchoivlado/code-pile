package com.example.codepile.services.services;

import com.example.codepile.data.converters.AuthorityConverter;
import com.example.codepile.data.entities.User;
import com.example.codepile.data.enums.Authority;
import com.example.codepile.data.models.binding.user.EditProfieBindingModel;
import com.example.codepile.data.models.service.user.*;
import com.example.codepile.data.repositories.UserRepository;
import com.example.codepile.error.user.EmailAlreadyExistsException;
import com.example.codepile.error.user.PasswordDontMatchException;
import com.example.codepile.error.user.UsernameAlreadyExistsException;
import com.example.codepile.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.codepile.data.validation.MessageCodes.*;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthority().getGrantedAuthorities());
    }

    @Override
    public void registerUser(UserServiceModel userServiceModel) {
        this.checkIfUserExistByUsernameAndEmail(userServiceModel.getUsername(), userServiceModel.getEmail());

        this.setProperAuthority(userServiceModel);
        this.setEncodedPassword(userServiceModel);

        User user = modelMapper.map(userServiceModel, User.class);
        this.userRepository.save(user);
    }

    @Override
    public List<UserServiceModel> getAllUsers() {
        List<User> users = this.userRepository.findAll();
        return users.stream().map(user -> modelMapper.map(user, UserServiceModel.class)).collect(Collectors.toList());
    }

    @Override
    public void changeAuthorityUser(ChangeUserAuthorityServiceModel model) {
        this.checkIfUserExistsWithId(model.getId());
        User user = this.userRepository.findUserById(model.getId());
        AuthorityConverter authorityConverter = new AuthorityConverter();
        Authority authority = authorityConverter.convertToEntityAttribute(model.getToRole());
        user.setAuthority(authority);
        this.userRepository.save(user);
    }

    @Override
    public ProfileServiceModel getProfile(String username) {
        this.checkIfUserExistsWithUserName(username);
        User user = this.userRepository.findUserByUsername(username);
        return modelMapper.map(user, ProfileServiceModel.class);
    }

    @Override
    public EditProfieBindingModel getEditProfile(String username) {
        this.checkIfUserExistsWithUserName(username);
        User user = this.userRepository.findUserByUsername(username);
        return modelMapper.map(user, EditProfieBindingModel.class);
    }

    @Override
    public void editProfile(EditProfileServiceModel model) {
        this.checkIfUserExistsWithUserName(model.getUsername());
        User user = this.userRepository.findUserByUsername(model.getUsername());

        this.checkPasswordsMatch(model.getPassword(), user);
        this.checkIfEmailIExists(user.getEmail(), model.getEmail());
        user.setEmail(model.getEmail());
        this.userRepository.save(user);
    }

    @Override
    public void changePassword(NewPasswordServiceModel serviceModel) {
        this.checkIfUserExistsWithUserName(serviceModel.getUsername());
        User user = this.userRepository.findUserByUsername(serviceModel.getUsername());
        this.checkPasswordsMatch(serviceModel.getOldPassword(), user);
        String encodedInputPassword = this.passwordEncoder.encode(serviceModel.getPassword());
        user.setPassword(encodedInputPassword);
        this.userRepository.save(user);
    }

    @Override
    public boolean checkUserExistsWithUsername(String username) {
        return this.userRepository.existsUserByUsername(username);
    }


    private void setEncodedPassword(UserServiceModel userServiceModel) {
        String passsword = userServiceModel.getPassword();
        userServiceModel.setPassword(passwordEncoder.encode(passsword));
    }

    private void setProperAuthority(UserServiceModel userServiceModel) {

        Authority authority = null;

        if (this.userRepository.findAll().size() > 0) {
            authority = Authority.USER;
        } else {
            authority = Authority.ROOT;
        }

        userServiceModel.setAuthority(authority);
    }

    private void checkIfUserExistByUsernameAndEmail(String username, String email) {
        boolean usernameAlreadyExists = this.userRepository.existsUserByUsername(username);
        if (usernameAlreadyExists) {
            throw new UsernameAlreadyExistsException(DUPLICATE_USERNAME);
        }

        boolean emailAlreadyExists = this.userRepository.existsUserByEmail(email);

        if (emailAlreadyExists) {
            throw new EmailAlreadyExistsException(DUPLICATE_EMAIL);
        }
    }

    private void checkIfUserExistsWithUserName(String username) {
        boolean existsUserWithUsrername = this.userRepository.existsUserByUsername(username);
        if (!existsUserWithUsrername) {
            throw new UsernameNotFoundException(USER_USERNAME_NOTFOUND);
        }
    }

    private void checkIfUserExistsWithId(String id) {
        boolean existsById = this.userRepository.existsById(id);
        if (!existsById) {
            throw new UsernameNotFoundException(USER_ID_NOTFOUND);
        }
    }

    private void checkPasswordsMatch(String inputPassword, User user) {
        if (!passwordEncoder.matches(inputPassword, user.getPassword())) {
            throw new PasswordDontMatchException(PASSWORD_DONT_MATCH);
        }
    }

    private void checkIfEmailIExists(String oldEmail, String newEmail) {
        if (!oldEmail.equals(newEmail)) {
            boolean userExistsWithEmail = this.userRepository.existsUserByEmail(newEmail);
            if (userExistsWithEmail) {
                throw new EmailAlreadyExistsException(DUPLICATE_EMAIL);
            }
        }
    }
}
