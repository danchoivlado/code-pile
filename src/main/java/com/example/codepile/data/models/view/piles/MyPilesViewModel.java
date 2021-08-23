package com.example.codepile.data.models.view.piles;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyPilesViewModel {
    private List<MyPileViewModel> piles;
}
