package com.fierceadventurer.apigateway.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KeycloakRoleConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {
    private final JwtGrantedAuthoritiesConverter defaultAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
        Collection<GrantedAuthority> defaultAuthorities = defaultAuthoritiesConverter.convert(jwt);

        List<GrantedAuthority> keycloakRoles = extractKeycloakRoles(jwt);

        List<GrantedAuthority> allAuthorities = Stream.concat(
                defaultAuthorities.stream(),
                keycloakRoles.stream()
        ).toList();

        String principalName = jwt.getClaimAsString("email");
        if(principalName == null) {
            principalName = jwt.getSubject();
        }

        return Mono.just(new JwtAuthenticationToken(jwt , allAuthorities , principalName));
    }

    private List<GrantedAuthority> extractKeycloakRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");

        if(realmAccess == null || realmAccess.isEmpty()) {
            return Collections.emptyList();
        }

        Collection<String> roles = (Collection<String>) realmAccess.get("roles");

        if(roles == null || roles.isEmpty()) {
            return Collections.emptyList();
        }

        return roles.stream()
                .map(roleName -> ROLE_PREFIX + roleName.toUpperCase())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
