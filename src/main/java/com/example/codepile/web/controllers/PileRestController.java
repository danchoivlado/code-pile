package com.example.codepile.web.controllers;

import com.example.codepile.data.models.bodyModels.CheckUserExistsWithUsernameBody;
import com.example.codepile.data.models.bodyModels.pile.ChangeEditorBody;
import com.example.codepile.data.models.bodyModels.pile.ChangeLanguageBody;
import com.example.codepile.data.models.bodyModels.pile.ChangeTitleBody;
import com.example.codepile.services.PileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pile")
@PreAuthorize("isAuthenticated()")
public class PileRestController {
    PileService pileService;

    public PileRestController(PileService pileService) {
        this.pileService = pileService;
    }

    @PostMapping("/changeLanguage")
    public ResponseEntity<?> changeLanguage(@RequestBody ChangeLanguageBody body){
        this.pileService.changeLanguage(body.getPileId(), body.getLanguage());

        return ResponseEntity.ok("");
    }

    @PostMapping("/changeTitle")
    public ResponseEntity<?> changeTitle(@RequestBody ChangeTitleBody body){
        this.pileService.changeTitle(body.getPileId(), body.getTitle());

        return ResponseEntity.ok("");
    }

    @PostMapping("/changeEditorText")
    public ResponseEntity<?> changeEditorText(@RequestBody ChangeEditorBody body){
        this.pileService.changeEditorText(body.getPileId(), body.getEditorText());

        return ResponseEntity.ok("");
    }
}
