package com.example.codepile.data.models.webSockets;

import com.example.codepile.data.models.webSockets.base.BasePileWebSocket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Language extends BasePileWebSocket {
    private String content;
}
