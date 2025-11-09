package com.fierceadventurer.aiservice.configuration;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors;

public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Object realmAccessObj = jwt.getClaims().get("realm_access");

        if(!(realmAccessObj instanceof Map)) {
            return new ArrayList<>();
        }
        Map<String,Object> realmAccess = (Map<String,Object>) realmAccessObj;

        Object rolesObj = realmAccess.get("roles");
        if(!(rolesObj instanceof List)) {
            return new ArrayList<>();
        }

        List<String> roles = (List<String>) rolesObj;

        return roles.stream().map(roleName -> ROLE_PREFIX + roleName.toUpperCase())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
