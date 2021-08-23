package com.example.codepile.data.entities;

import com.example.codepile.data.converters.AceConverter;
import com.example.codepile.data.converters.AuthorityConverter;
import com.example.codepile.data.entities.base.BaseLongEntity;
import com.example.codepile.data.enums.AceMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "piles"
)
public class Pile {
        public Pile(String id, AceMode aceMode, String title, boolean readOnly, String pileText, User user) {
                this.id = id;
                this.aceMode = aceMode;
                this.title = title;
                this.readOnly = readOnly;
                this.pileText = pileText;
                this.user = user;
        }

        public Pile(String id, AceMode aceMode, String title, boolean readOnly, String pileText) {
                this.id = id;
                this.aceMode = aceMode;
                this.title = title;
                this.readOnly = readOnly;
                this.pileText = pileText;
        }

        @Id
        @Column(unique = true, nullable = false, insertable = false, updatable = false)
        private String id;

        @Column
        @Convert(converter = AceConverter.class)
        private AceMode aceMode;

        @Column(nullable = true)
        private String title;

        private boolean readOnly;

        private String pileText;

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User user;
}
