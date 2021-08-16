package com.example.codepile.web.controllers;

import com.example.codepile.data.models.binding.User.UserRegisterBindingModel;
import com.example.codepile.data.models.service.ProfileServiceModel;
import com.example.codepile.data.models.service.UserServiceModel;
import com.example.codepile.data.models.view.ProfileViewModel;
import com.example.codepile.data.models.view.UserViewModel;
import com.example.codepile.services.UserService;
import com.example.codepile.web.controllers.base.BaseController;
import org.apache.catalina.LifecycleState;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UsersControllers extends BaseController {

    private ModelMapper modelMapper;
    private UserService userService;

    public UsersControllers(ModelMapper modelMapper, UserService userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @GetMapping("login")
    @PreAuthorize("isAnonymous()")
    public ModelAndView login(){
        return view("/users/login");
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView register(@ModelAttribute(name = "model") UserRegisterBindingModel model){
        return view("users/register");
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView registerConfirm(@Valid @ModelAttribute(name = "model") UserRegisterBindingModel model,
                                        BindingResult bindingResult){
        if(this.passwordsNotMatch(model.getPassword(), model.getConfirmPassword())){
            bindingResult.addError(new FieldError("model", "password", "Passwords don't match."));
        }
        if (bindingResult.hasErrors()) {
            return view("users/register");
        }

        UserServiceModel userServiceModel = modelMapper.map(model, UserServiceModel.class);
        this.userService.registerUser(userServiceModel);

        return redirect("/users/login");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView getAll(ModelAndView modelAndView){
        List<UserServiceModel> userServiceModels = this.userService.getAllUsers();
        List<UserViewModel> viewModels = userServiceModels.stream()
                .map(user -> {
                    UserViewModel userViewModel = modelMapper.map(user,UserViewModel.class);
                    userViewModel.setAuthorities(
                            user.getAuthority()
                                    .getGrantedAuthorities()
                                    .stream().
                                    map(authority -> authority.name()).collect(Collectors.toList()));
                    return userViewModel;
                }).collect(Collectors.toList());
        modelAndView.addObject("users",viewModels);

        return super.view("users/all-users", modelAndView);
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView getProfile(Principal principal, ModelAndView modelAndView){
        ProfileServiceModel profileServiceModel = this.userService.getProfile(principal.getName());
        ProfileViewModel model = modelMapper.map(profileServiceModel, ProfileViewModel.class);
        modelAndView.addObject("model", model);
        return super.view("users/profile",modelAndView);
    }


    private boolean passwordsNotMatch(String password, String confirmPassword) {
        return !password.equals(confirmPassword);
    }
}
