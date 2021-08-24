package com.example.codepile.data.models.webSockets;

import com.example.codepile.data.models.webSockets.base.BasePileWebSocket;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessMode extends BasePileWebSocket {
    private boolean readOnly;
}
