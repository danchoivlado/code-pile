package com.example.codepile.web.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/users")
public class UsersControllers extends BaseController {

    @GetMapping("login")
    @PreAuthorize("isAnonymous()")
    public ModelAndView login(){
        return view("/users/login");
    }
}
