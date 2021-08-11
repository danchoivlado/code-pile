package com.example.codepile.data.entities.base;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Setter(AccessLevel.PRIVATE)
@Getter
@MappedSuperclass
public abstract class BaseUuidEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(unique = true, nullable = false, insertable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

}
