package com.example.codepile.web.controllers;

import com.example.codepile.data.models.webSockets.Title;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class PileWebSocketController {
    @MessageMapping("/titleSend")
    @SendTo("/topic/titleRecieve")
    public Title title(Title title) throws InterruptedException {
        return title;
    }
}
