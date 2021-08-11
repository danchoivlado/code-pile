package com.example.codepile.data.entities.base;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter(AccessLevel.PRIVATE)
@Getter
@MappedSuperclass
public class BaseLongEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, insertable = false, updatable = false)
    private Long id;
}
