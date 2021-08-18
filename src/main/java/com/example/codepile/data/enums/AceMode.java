package com.example.codepile.data.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public enum AceMode {

    HTML("html"),
    JAVA("java");

    private final String id;
    private final String name;

    AceMode(String id){
        this.id = id;
        this.name = convertIdToName(id);
    }

    private static final List<AceMode> ACE_MODES_LIST = Arrays.asList(AceMode.values());

    private final String convertIdToName(String id){
        return id.substring(0,1).toUpperCase() + id.substring(1);
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public static List<AceMode> getAceModesList(){
        return ACE_MODES_LIST;
    }
}
