package com.example.codepile.data.converters;

import com.example.codepile.data.enums.Authority;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class AuthorityConverter implements AttributeConverter<Authority, String> {

    @Override
    public String convertToDatabaseColumn(Authority authority) {
        return authority.getId();
    }

    @Override
    public Authority convertToEntityAttribute(String id) {
        return Authority.fromId(id);
    }
}
