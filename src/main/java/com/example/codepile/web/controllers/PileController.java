package com.example.codepile.web.controllers;

import com.example.codepile.data.enums.AceMode;
import com.example.codepile.data.models.service.pile.MyPilesServiceViewModel;
import com.example.codepile.data.models.service.pile.PileCreateServiceModel;
import com.example.codepile.data.models.service.pile.PileServiceModel;
import com.example.codepile.data.models.view.piles.MyPileViewModel;
import com.example.codepile.data.models.view.piles.MyPilesViewModel;
import com.example.codepile.data.models.view.piles.PileViewModel;
import com.example.codepile.services.PileService;
import com.example.codepile.web.controllers.base.BaseController;
import org.hibernate.property.access.internal.PropertyAccessStrategyNoopImpl;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/pile")
public class PileController extends BaseController {
    private static final String aceModeObjectName = "aceModes";
    private static final String defaultTitleObjectName = "defaultTitle";
    private static final String defaultLanguageObjectName = "defaultLanguage";
    private static final String myPilesObjectName = "myPiles";
    private static final String pileObjectName = "pile";
    private PileService pileService;
    private ModelMapper modelMapper;

    public PileController(PileService pileService, ModelMapper modelMapper) {
        this.pileService = pileService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{pileId}")
    public ModelAndView getPile(Principal principal, ModelAndView modelAndView, @PathVariable() String pileId) {
        PileServiceModel pileServiceModel = this.pileService.getPileWithId(pileId);

        PileViewModel pileViewModel = modelMapper.map(pileServiceModel, PileViewModel.class);
        pileViewModel.setUserUsername(pileServiceModel.getUser().getUsername());
        pileViewModel.setUserUserId(pileServiceModel.getUser().getId());
        pileViewModel.setAceModes(AceMode.getAceModesList());
        modelAndView.addObject(pileObjectName,pileViewModel);

        return super.view("pile", modelAndView);
    }

    @PostMapping("")
    public ModelAndView createPile(Principal principal) {
        PileCreateServiceModel serviceModel = this.pileService.createPile(principal.getName());
        return super.redirect("/pile/" + serviceModel.getPileId());
    }

    @GetMapping("/my-piles")
    public ModelAndView getMyPiles(ModelAndView modelAndView, Principal principal) {
        MyPilesServiceViewModel myPilesServiceViewModel = this.pileService.getMyPiles(principal.getName());
        List<MyPileViewModel> myPileViewModelList = myPilesServiceViewModel
                .getPiles()
                .stream()
                .map(myPileServiceViewModel -> modelMapper.map(myPileServiceViewModel, MyPileViewModel.class))
                .collect(Collectors.toList());

        MyPilesViewModel myPilesViewModel = new MyPilesViewModel(myPileViewModelList);
        modelAndView.addObject(myPilesObjectName, myPilesViewModel);
        return super.view("my-piles", modelAndView);
    }

    @PostMapping("/delete/{pileId}")
    public ModelAndView deletePile(@PathVariable() String pileId) {
        this.pileService.deletePileWithId(pileId);
        return super.redirect("/pile/my-piles");
    }

}
