package com.example.codepile.data.converters;

import com.example.codepile.data.enums.AceMode;
import com.example.codepile.data.enums.Authority;
import com.example.codepile.error.ace.AceNotFoundException;
import com.example.codepile.error.authority.AuthorityNotFoundException;

import javax.persistence.AttributeConverter;

import static com.example.codepile.data.validation.MessageCodes.ACE_ID_NOTFOUND;
import static com.example.codepile.data.validation.MessageCodes.AUTHORITY_ID_NOTFOUND;

public class AceConverter implements AttributeConverter<AceMode, String> {
    @Override
    public String convertToDatabaseColumn(AceMode aceMode) {
        return aceMode.getId();
    }

    @Override
    public AceMode convertToEntityAttribute(String id) {
        AceMode aceMode = AceMode.fromId(id);
        if (aceMode == null){
            throw new AceNotFoundException(ACE_ID_NOTFOUND);
        }

        return aceMode;
    }
}
