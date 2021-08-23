package com.example.codepile.data.models.bodyModels.pile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangeLanguageBody {
    private String pileId;
    private String language;
}
