package com.example.codepile.services;

import com.example.codepile.data.models.service.pile.PileCreateServiceModel;
import com.example.codepile.data.models.service.pile.PileServiceModel;

public interface PileService {
    PileServiceModel getPileWithId(String id);
    PileCreateServiceModel createPile(String byUserWithUsername);
}
