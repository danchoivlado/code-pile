package com.example.codepile.data.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum AceMode {

    HTML("html"),
    JAVA("java"),
    PYTHON("python"),
    CSS("css"),
    SQL("sql"),
    CSHARP("csharp"),
    RUST("rust"),
    PERL("perl"),
    JAVASCRIPT("javascript");

    private final String id;
    private final String name;

    AceMode(String id){
        this.id = id;
        this.name = convertIdToName(id);
    }

    public static AceMode fromId(String id) {
        return STRING_TO_ENUM.get(id);
    }

    private static final Map<String, AceMode> STRING_TO_ENUM = Stream
            .of(AceMode.values())
            .collect(Collectors.toUnmodifiableMap(AceMode::getId, aceMode -> aceMode));

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
