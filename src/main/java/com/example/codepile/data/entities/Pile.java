package com.example.codepile.data.entities;

import com.example.codepile.data.entities.base.BaseLongEntity;
import com.example.codepile.data.validation.annotations.composite.pile.ValidPileTitle;
import com.example.codepile.data.validation.annotations.composite.user.ValidUserUsername;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "piles",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_piles_link", columnNames = {"link"})
        }
)
public class Pile extends BaseLongEntity {

        @ValidPileTitle
        @Column(nullable = true, length = ValidUserUsername.MAX_LENGTH)
        private String title;

        private boolean readOnly;

        private String pileText;

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        private String link;
}
