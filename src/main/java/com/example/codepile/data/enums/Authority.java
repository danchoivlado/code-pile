package com.example.codepile.data.enums;

import org.springframework.security.core.GrantedAuthority;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Authority implements GrantedAuthority {

    ROOT("root"),
    ADMIN("admin"),
    USER("user");



    private static final Map<String, Authority> STRING_TO_ENUM = Stream
            .of(Authority.values())
            .collect(Collectors.toUnmodifiableMap(Authority::getId, authority -> authority));

    private static final Map<Authority, Set<Authority>> GRANTED_AUTHORITIES = Stream
            .of(Authority.values())
            .collect(Collectors.toMap(
                    k -> k,
                    k -> EnumSet.range(k, Authority.USER)));

    private static final String ROLE_PREFIX = "ROLE_";

    private final String role;
    private final String id;

    Authority(String id) {
        this.id = id;
        role = ROLE_PREFIX + name();
    }


    public static Authority fromId(String id) {
        return STRING_TO_ENUM.get(id);
    }


    public String getId() {
        return id;
    }

    @Override
    public String getAuthority() {
        return role;
    }

    public Set<Authority> getGrantedAuthorities() {
        return GRANTED_AUTHORITIES.get(this);
    }
}