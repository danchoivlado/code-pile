package com.example.codepile.data.entities;

import com.example.codepile.data.entities.base.BaseUuidEntity;
import com.example.codepile.data.enums.Authority;
import com.example.codepile.data.validation.annotations.composite.user.ValidUserAuthority;
import com.example.codepile.data.validation.annotations.composite.user.ValidUserEncryptedPassword;
import com.example.codepile.data.validation.annotations.composite.user.ValidUserUsername;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
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

    @ValidUserUsername
    @Column(nullable = false, updatable = false, length = ValidUserUsername.MAX_LENGTH)
    private String username;

    @ValidUserEncryptedPassword
    @Column(nullable = false, length = ValidUserEncryptedPassword.MAX_LENGTH)
    private String password;

    @Setter
    @ValidUserAuthority
    @Convert(converter = AuthorityConverter.class)
    @Column(nullable = false, length = ValidUserAuthority.MAX_LENGTH)
    private Authority authority;

    @Setter
    private boolean active;

}
