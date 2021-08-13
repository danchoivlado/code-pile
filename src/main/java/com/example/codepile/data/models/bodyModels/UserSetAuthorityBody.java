package com.example.codepile.data.models.bodyModels;

import com.example.codepile.data.converters.AuthorityConverter;
import com.example.codepile.data.enums.Authority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Convert;
import javax.persistence.Converter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSetAuthorityBody {
    private String toRole;
    private String id;
}
