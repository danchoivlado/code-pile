package com.example.codepile.services.services;

import com.example.codepile.data.entities.User;
import com.example.codepile.data.enums.Authority;
import com.example.codepile.data.models.service.UserServiceModel;
import com.example.codepile.data.repositories.UserRepository;
import com.example.codepile.error.user.EmailAlreadyExistsException;
import com.example.codepile.error.user.UsernameAlreadyExistsException;
import com.example.codepile.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.standard.inline.StandardHTMLInliner;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.codepile.data.validation.MessageCodes.DUPLICATE_EMAIL;
import static com.example.codepile.data.validation.MessageCodes.DUPLICATE_USERNAME;

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
    public void setAuthorityUserToUser(String userId) {
        if (!this.userRepository.existsUserById(userId)){
            throw new UsernameNotFoundException("User with this ID NOT exists");
        }
        User user = this.userRepository.findUserById(userId);
        user.setAuthority(Authority.USER);
        this.userRepository.save(user);
    }


    private void setEncodedPassword(UserServiceModel userServiceModel){
        String passsword = userServiceModel.getPassword();
        userServiceModel.setPassword(passwordEncoder.encode(passsword));
    }

    private void setProperAuthority(UserServiceModel userServiceModel){

        Authority authority = null;

        if (this.userRepository.findAll().size() > 0){
            authority =Authority.USER;
        }
        else {
            authority = Authority.ROOT;
        }

        userServiceModel.setAuthority(authority);
    }

    private void checkIfUserExistByUsernameAndEmail(String username, String email){
        boolean usernameAlreadyExists = this.userRepository.existsUserByUsername(username);
       if (usernameAlreadyExists){
           throw new UsernameAlreadyExistsException(DUPLICATE_USERNAME);
       }

        boolean emailAlreadyExists = this.userRepository.existsUserByEmail(email);

       if (emailAlreadyExists){
           throw new EmailAlreadyExistsException(DUPLICATE_EMAIL);
       }
    }
}
