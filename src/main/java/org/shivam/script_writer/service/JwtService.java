package org.shivam.script_writer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    @Value("${app.jwt.expiration-minutes}")
    private long expirationMinutes;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    // Used after normal username/password login
    public String generateToken(Authentication authentication) {
        return buildToken(
                authentication.getName(),
                authentication.getAuthorities()
        );
    }

    // Used after validating a refresh token
    public String generateToken(UserDetails userDetails) {
        return buildToken(
                userDetails.getUsername(),
                userDetails.getAuthorities()
        );
    }

    // Contains the common JWT generation logic
    private String buildToken(
            String username,
            Collection<? extends GrantedAuthority> grantedAuthorities
    ) {
        Instant now = Instant.now();

        String authorities = grantedAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("script-writer-backend")
                .issuedAt(now)
                .expiresAt(
                        now.plus(
                                expirationMinutes,
                                ChronoUnit.MINUTES
                        )
                )
                .subject(username)
                .claim("authorities", authorities)
                .build();

        JwsHeader headers =
                JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder
                .encode(JwtEncoderParameters.from(headers, claims))
                .getTokenValue();
    }
}