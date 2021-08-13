package com.example.codepile.web.controllers;

import com.example.codepile.web.controllers.base.BaseController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class HomeController extends BaseController {

    @GetMapping({"/", "/home"})
    @PreAuthorize("permitAll()")
    public ModelAndView index(Principal principal){

        return super.view("index");
    }
}
