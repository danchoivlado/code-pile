package com.example.codepile.web.controllers;

import com.example.codepile.data.models.bodyModels.CheckUserExistsWithUsernameBody;
import com.example.codepile.data.models.bodyModels.pile.ChangeEditorBody;
import com.example.codepile.data.models.bodyModels.pile.ChangeLanguageBody;
import com.example.codepile.data.models.bodyModels.pile.ChangeTitleBody;
import com.example.codepile.error.pile.PileCannotBeEdited;
import com.example.codepile.services.PileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/pile")
public class PileRestController {
    PileService pileService;

    public PileRestController(PileService pileService) {
        this.pileService = pileService;
    }

    @PostMapping("/changeLanguage")
    public ResponseEntity<?> changeLanguage(@RequestBody ChangeLanguageBody body, Principal principal){
        this.checkIfUserCanEdit(principal, body.getPileId());
        this.pileService.changeLanguage(body.getPileId(), body.getLanguage());

        return ResponseEntity.ok("");
    }

    @PostMapping("/changeTitle")
    public ResponseEntity<?> changeTitle(@RequestBody ChangeTitleBody body, Principal principal){
        this.checkIfUserCanEdit(principal, body.getPileId());
        this.pileService.changeTitle(body.getPileId(), body.getTitle());

        return ResponseEntity.ok("");
    }

    @PostMapping("/changeEditorText")
    public ResponseEntity<?> changeEditorText(@RequestBody ChangeEditorBody body, Principal principal){
        this.checkIfUserCanEdit(principal, body.getPileId());
        this.pileService.changeEditorText(body.getPileId(), body.getEditorText());

        return ResponseEntity.ok("");
    }

    private void checkIfUserCanEdit(Principal principal, String pileId){
        if(!this.pileService.canCurrentUserEdit(principal,pileId)){
            throw new PileCannotBeEdited("You can't edit this Pile");
        }
    }
}
