package com.example.codepile.data.converters;

import com.example.codepile.data.enums.Authority;
import com.example.codepile.error.authority.AuthorityNotFoundException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static com.example.codepile.data.validation.MessageCodes.AUTHORITY_ID_NOTFOUND;

@Converter
public class AuthorityConverter implements AttributeConverter<Authority, String> {

    @Override
    public String convertToDatabaseColumn(Authority authority) {
        return authority.getId();
    }

    @Override
    public Authority convertToEntityAttribute(String id) {
        Authority authority = Authority.fromId(id);
        if (authority == null){
            throw new AuthorityNotFoundException(AUTHORITY_ID_NOTFOUND);
        }
        return authority;
    }
}
