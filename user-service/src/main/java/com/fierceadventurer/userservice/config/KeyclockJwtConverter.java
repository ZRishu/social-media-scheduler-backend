package com.fierceadventurer.userservice.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.stream.Stream;


public class KeyclockJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    private final KeyclockRoleConverter keyclockRoleConverter = new KeyclockRoleConverter();

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> defaultAuthorities = defaultGrantedAuthoritiesConverter.convert(jwt);

        Collection<GrantedAuthority> keyclockRoles = keyclockRoleConverter.convert(jwt);

        Collection<GrantedAuthority> allAuthorities = Stream.concat(
                defaultAuthorities.stream(), keyclockRoles.stream()).toList();

        String principalName = jwt.getClaimAsString("email");
        if(principalName == null) {
            principalName = jwt.getClaimAsString("preferred_username");
        }
        return new JwtAuthenticationToken(jwt , allAuthorities , principalName);
    }
}
