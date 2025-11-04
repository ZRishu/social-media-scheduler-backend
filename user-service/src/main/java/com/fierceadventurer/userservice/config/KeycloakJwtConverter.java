package com.fierceadventurer.userservice.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.stream.Stream;


public class KeycloakJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    private final KeycloakRoleConverter keycloakRoleConverter = new KeycloakRoleConverter();

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> defaultAuthorities = defaultGrantedAuthoritiesConverter.convert(jwt);

        Collection<GrantedAuthority> keycloakRoles = keycloakRoleConverter.convert(jwt);

        Collection<GrantedAuthority> allAuthorities = Stream.concat(
                defaultAuthorities.stream(), keycloakRoles.stream()).toList();

        String principalName = jwt.getClaimAsString("email");
        if(principalName == null) {
            principalName = jwt.getClaimAsString("preferred_username");
        }
        return new JwtAuthenticationToken(jwt , allAuthorities , principalName);
    }
}
