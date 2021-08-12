package com.example.codepile.data.entities;

import com.example.codepile.data.converters.AuthorityConverter;
import com.example.codepile.data.entities.base.BaseUuidEntity;
import com.example.codepile.data.enums.Authority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_username", columnNames = {"username"})
        }
)
public class User extends BaseUuidEntity {

    @Column(nullable = false, updatable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Convert(converter = AuthorityConverter.class)
    @Column(nullable = false)
    private Authority authority;

    @OneToMany(mappedBy = "user")
    private Set<Pile> piles;

    private boolean active;

    @Column(nullable = false)
    private String email;
}
