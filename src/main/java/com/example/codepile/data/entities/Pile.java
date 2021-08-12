package com.example.codepile.data.entities;

import com.example.codepile.data.entities.base.BaseLongEntity;
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

        @Column(nullable = true)
        private String title;

        private boolean readOnly;

        private String pileText;

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        private String link;
}
