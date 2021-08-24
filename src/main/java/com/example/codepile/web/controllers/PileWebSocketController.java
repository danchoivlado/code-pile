package com.example.codepile.web.controllers;

import com.example.codepile.data.models.webSockets.Title;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class PileWebSocketController {
    @MessageMapping("/title/{pileId}")
    @SendTo("/title/{pileId}")
    public Title title(@DestinationVariable String pileId, Title title) throws InterruptedException {
        return title;
    }
}
