package com.example.codepile.web.controllers;

import com.example.codepile.data.models.bodyModels.UserSetAuthorityBody;
import com.example.codepile.data.models.service.ChangeUserAuthorityServiceModel;
import com.example.codepile.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    UserService userService;
    private ModelMapper modelMapper;

    public UserRestController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/set-user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String setUser(@RequestBody UserSetAuthorityBody userSetAuthorityBody){
        ChangeUserAuthorityServiceModel model = modelMapper.map(userSetAuthorityBody, ChangeUserAuthorityServiceModel.class);
        this.userService.changeAuthorityUser(model);
        return "12";
    }
}
