package com.example.codepile.services;

import com.example.codepile.data.models.service.pile.ChangeAccessModeServiceModel;
import com.example.codepile.data.models.service.pile.MyPilesServiceViewModel;
import com.example.codepile.data.models.service.pile.PileCreateServiceModel;
import com.example.codepile.data.models.service.pile.PileServiceModel;
import com.example.codepile.data.models.view.piles.PileViewModel;

import java.security.Principal;

public interface PileService {
    PileServiceModel getPileWithId(String id);
    PileCreateServiceModel createPile(String byUserWithUsername);
    MyPilesServiceViewModel getMyPiles(String byUserWithUsername);
    void deletePileWithId(String pileId);
    void changeTitle(String pileId, String title);
    void changeLanguage(String pileId, String language);
    void changeEditorText(String pileId, String editorText);
    boolean isCurrentUserOwner(Principal principal, String pileUserId);
    boolean canCurrentUserEdit(Principal principal, String pileId);
    boolean canCurrentUserChangeMode(Principal principal, String pileId);
    ChangeAccessModeServiceModel changeAccessMode(boolean readOnly, String pileId, Principal principal);
}
