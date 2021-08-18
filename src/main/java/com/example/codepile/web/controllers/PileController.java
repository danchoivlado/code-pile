package com.example.codepile.web.controllers;

import com.example.codepile.data.enums.AceMode;
import com.example.codepile.web.controllers.base.BaseController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@PreAuthorize("isAuthenticated()")
public class PileController extends BaseController {
    private static final String aceModeObjectName = "aceModes";

    @GetMapping("/pile")
    public ModelAndView getPile(ModelAndView modelAndView){
        modelAndView.addObject(aceModeObjectName, AceMode.getAceModesList());

        return super.view("pile",modelAndView);
    }
}
