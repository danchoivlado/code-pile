package com.example.codepile.web.controllers;

import com.example.codepile.data.models.binding.User.UserRegisterBindingModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UsersControllers extends BaseController {

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
//        UserServiceModel serviceModel = mapper.map(model, UserServiceModel.class);
//        userService.registerUser(serviceModel);

        return redirect("/users/login");
    }
    private boolean passwordsNotMatch(String password, String confirmPassword) {
        return !password.equals(confirmPassword);
    }
}
