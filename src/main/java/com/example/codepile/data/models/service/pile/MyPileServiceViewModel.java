package com.example.codepile.data.models.service.pile;

import com.example.codepile.data.enums.AceMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MyPileServiceViewModel {
    private String id;
    private String title;
    private AceMode aceMode;
}
