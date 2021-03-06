package com.example.codepile.web.controllers;

import com.example.codepile.data.entities.Pile;
import com.example.codepile.data.models.service.pile.ChangeAccessModeServiceModel;
import com.example.codepile.data.models.webSockets.*;
import com.example.codepile.error.pile.PileCannotBeEdited;
import com.example.codepile.services.PileService;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.thymeleaf.standard.inline.StandardHTMLInliner;

import java.security.Principal;

@Controller
public class PileWebSocketController {
    private PileService pileService;
    private ModelMapper modelMapper;

    public PileWebSocketController(PileService pileService, ModelMapper modelMapper) {
        this.pileService = pileService;
        this.modelMapper = modelMapper;
    }

    @MessageMapping("/editorText/{subscriber}")
    @SendTo("/editorText/{subscriber}")
    public Editor title(@DestinationVariable String subscriber, Editor editor, Principal principal) throws InterruptedException {
        this.checkIfUserCanEdit(principal, editor.getPileId());
        this.pileService.changeEditorText(editor.getPileId(), editor.getContent());
        return editor;
    }

    @MessageMapping("/language/{subscriber}")
    @SendTo("/language/{subscriber}")
    public Language title(@DestinationVariable String subscriber, Language language, Principal principal) throws InterruptedException {
        this.checkIfUserCanEdit(principal, language.getPileId());
        this.pileService.changeLanguage(language.getPileId(), language.getContent());
        return language;
    }

    @MessageMapping("/title/{subscriber}")
    @SendTo("/title/{subscriber}")
    public Title title(@DestinationVariable String subscriber, Title titleObj, Principal principal) throws InterruptedException {
        this.checkIfUserCanEdit(principal, titleObj.getPileId());
        this.pileService.changeTitle(titleObj.getPileId(), titleObj.getContent());
        return titleObj;
    }

    @MessageMapping("/accessMode/{subscriber}")
    @SendTo("/accessMode/{subscriber}")
    public AccessModeResponse title(@DestinationVariable String subscriber, AccessMode accessMode, Principal principal) throws InterruptedException {
        this.checkIfUserCanChangeMode(principal, accessMode.getPileId());
        ChangeAccessModeServiceModel serviceModel = this.pileService.changeAccessMode(accessMode.isReadOnly(), accessMode.getPileId(), principal);
        return modelMapper.map(serviceModel, AccessModeResponse.class);
    }

    private void checkIfUserCanChangeMode(Principal principal, String pileId){
        if(!this.pileService.canCurrentUserChangeMode(principal,pileId)){
            throw new PileCannotBeEdited("You can't edit this Pile");
        }
    }

    private void checkIfUserCanEdit(Principal principal, String pileId){
        if(!this.pileService.canCurrentUserEdit(principal,pileId)){
            throw new PileCannotBeEdited("You can't edit this Pile");
        }
    }
}
