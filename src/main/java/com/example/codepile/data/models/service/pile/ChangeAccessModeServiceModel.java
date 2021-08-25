package com.example.codepile.data.models.service.pile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeAccessModeServiceModel {
    private boolean readOnly;
    private String subscription;
    private boolean isOwner;
}
