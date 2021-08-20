package com.example.codepile.data.converters;

import com.example.codepile.data.enums.AceMode;
import com.example.codepile.data.enums.Authority;

import javax.persistence.AttributeConverter;

public class AceConverter implements AttributeConverter<AceMode, String> {
    @Override
    public String convertToDatabaseColumn(AceMode aceMode) {
        return aceMode.getId();
    }

    @Override
    public AceMode convertToEntityAttribute(String id) {
        return AceMode.fromId(id);
    }
}
