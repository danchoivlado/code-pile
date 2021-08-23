package com.example.codepile.services;

import com.example.codepile.data.models.service.pile.MyPilesServiceViewModel;
import com.example.codepile.data.models.service.pile.PileCreateServiceModel;
import com.example.codepile.data.models.service.pile.PileServiceModel;
import com.example.codepile.data.models.view.piles.PileViewModel;

public interface PileService {
    PileServiceModel getPileWithId(String id);
    PileCreateServiceModel createPile(String byUserWithUsername);
    MyPilesServiceViewModel getMyPiles(String byUserWithUsername);
    void deletePileWithId(String pileId);
}
