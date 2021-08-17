package com.example.codepile.web.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@PreAuthorize("isAuthenticated()")
public class PileController {

    @GetMapping("/pile")
    public String getPile(){
        return"pile";
    }
}
