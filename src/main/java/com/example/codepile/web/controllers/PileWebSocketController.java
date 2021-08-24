package com.example.codepile.web.controllers;

import com.example.codepile.data.models.webSockets.AccessMode;
import com.example.codepile.data.models.webSockets.Title;
import com.example.codepile.error.pile.PileCannotBeEdited;
import com.example.codepile.services.PileService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.thymeleaf.standard.inline.StandardHTMLInliner;

import java.security.Principal;

@Controller
public class PileWebSocketController {
    private PileService pileService;

    public PileWebSocketController(PileService pileService) {
        this.pileService = pileService;
    }

    @MessageMapping("/title/{subscriber}")
    @SendTo("/title/{subscriber}")
    public Title title(@DestinationVariable String subscriber, Title titleObj, Principal principal) throws InterruptedException {
        this.checkIfUserCanEdit(principal, titleObj.getPileId());
        return titleObj;
    }

    @MessageMapping("/accessMode/{subscriber}")
    @SendTo("/accessMode/{subscriber}")
    public AccessMode title(@DestinationVariable String subscriber, AccessMode accessMode, Principal principal) throws InterruptedException {
        this.checkIfUserCanChangeMode(principal, accessMode.getPileId());
        return accessMode;
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
    }}
