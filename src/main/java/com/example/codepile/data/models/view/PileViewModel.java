package com.example.codepile.data.models.view;

import com.example.codepile.data.entities.User;
import com.example.codepile.data.enums.AceMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PileViewModel {
    private String userUsername;
    private String userUserId;
    private String id;
    private String title;
    private AceMode aceMode;
    private boolean readOnly;
    private String pileText;
    private String linkId;
}
