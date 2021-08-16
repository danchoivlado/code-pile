package com.example.codepile.web.controllers;

import com.example.codepile.data.models.binding.user.EditProfieBindingModel;
import com.example.codepile.data.models.binding.user.NewPasswordBindingPassword;
import com.example.codepile.data.models.binding.user.UserRegisterBindingModel;
import com.example.codepile.data.models.service.user.EditProfileServiceModel;
import com.example.codepile.data.models.service.user.NewPasswordServiceModel;
import com.example.codepile.data.models.service.user.ProfileServiceModel;
import com.example.codepile.data.models.service.user.UserServiceModel;
import com.example.codepile.data.models.view.ProfileViewModel;
import com.example.codepile.data.models.view.UserViewModel;
import com.example.codepile.services.UserService;
import com.example.codepile.web.controllers.base.BaseController;
import org.dom4j.rule.Mode;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
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
    public ModelAndView login() {
        return view("/users/login");
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView register(@ModelAttribute(name = "model") UserRegisterBindingModel model) {
        return view("users/register");
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView registerConfirm(@Valid @ModelAttribute(name = "model") UserRegisterBindingModel model,
                                        BindingResult bindingResult) {
        if (this.passwordsNotMatch(model.getPassword(), model.getConfirmPassword())) {
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
    public ModelAndView getAll(ModelAndView modelAndView) {
        List<UserServiceModel> userServiceModels = this.userService.getAllUsers();
        List<UserViewModel> viewModels = userServiceModels.stream()
                .map(user -> {
                    UserViewModel userViewModel = modelMapper.map(user, UserViewModel.class);
                    userViewModel.setAuthorities(
                            user.getAuthority()
                                    .getGrantedAuthorities()
                                    .stream().
                                    map(authority -> authority.name()).collect(Collectors.toList()));
                    return userViewModel;
                }).collect(Collectors.toList());
        modelAndView.addObject("users", viewModels);

        return super.view("users/all-users", modelAndView);
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView getProfile(Principal principal, ModelAndView modelAndView) {
        ProfileServiceModel profileServiceModel = this.userService.getProfile(principal.getName());
        ProfileViewModel model = modelMapper.map(profileServiceModel, ProfileViewModel.class);
        modelAndView.addObject("model", model);
        return super.view("users/profile", modelAndView);
    }

    @GetMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editProfile(Principal principal, ModelAndView modelAndView) {
        ProfileServiceModel profileServiceModel = this.userService.getProfile(principal.getName());
        EditProfieBindingModel model = modelMapper.map(profileServiceModel, EditProfieBindingModel.class);
        model.setPassword("");
        modelAndView.addObject("model", model);
        return super.view("users/edit-profile", modelAndView);
    }

    @PostMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editProfileConfirm(@Valid @ModelAttribute(name = "model") EditProfieBindingModel bindingModel,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return super.view("users/edit-profile");
        }

        EditProfileServiceModel serviceModel = modelMapper.map(bindingModel, EditProfileServiceModel.class);
        this.userService.editProfile(serviceModel);

        return super.redirect("/users/profile");
    }

    @GetMapping("/new-password")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView changePassword(@ModelAttribute("model")NewPasswordBindingPassword newPasswordBindingPassword){
        return super.view("/users/new-password");
    }

    @PostMapping("/new-password")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView changePasswordConfirm(@Valid @ModelAttribute(name = "model") NewPasswordBindingPassword bindingModel,
                                           BindingResult bindingResult, Principal principal) {

        if (this.passwordsNotMatch(bindingModel.getPassword(), bindingModel.getConfirmPassword())) {
            bindingResult.addError(new FieldError("model", "password", "Passwords don't match."));
        }

        if (bindingResult.hasErrors()) {
            return super.view("users/new-password");
        }

        NewPasswordServiceModel serviceModel = modelMapper.map(bindingModel, NewPasswordServiceModel.class);
        serviceModel.setUsername(principal.getName());
        this.userService.changePassword(serviceModel);

        return super.redirect("/users/profile");
    }

    private boolean passwordsNotMatch(String password, String confirmPassword) {
        return !password.equals(confirmPassword);
    }
}
