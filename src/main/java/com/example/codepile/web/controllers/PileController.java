package com.example.codepile.web.controllers;

import com.example.codepile.data.enums.AceMode;
import com.example.codepile.data.models.service.pile.PileServiceModel;
import com.example.codepile.data.models.view.PileViewModel;
import com.example.codepile.services.PileService;
import com.example.codepile.web.controllers.base.BaseController;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
@PreAuthorize("isAuthenticated()")
public class PileController extends BaseController {
    private static final String aceModeObjectName = "aceModes";
    private PileService pileService;
    private ModelMapper modelMapper;

    public PileController(PileService pileService, ModelMapper modelMapper) {
        this.pileService = pileService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/pile/{pileId}")
    public ModelAndView getPile(Principal principal, ModelAndView modelAndView, @PathVariable() String pileId){
        PileServiceModel pileServiceModel = this.pileService.getPileWithId(pileId);

        PileViewModel pileViewModel = modelMapper.map(pileServiceModel, PileViewModel.class);
        pileViewModel.setUserUsername(pileServiceModel.getUser().getUsername());
        pileViewModel.setUserUserId(pileServiceModel.getUser().getId());

        modelAndView.addObject(aceModeObjectName, AceMode.getAceModesList());
        modelAndView.addObject(aceModeObjectName, AceMode.getAceModesList());

        return super.view("pile",modelAndView);
    }
}
