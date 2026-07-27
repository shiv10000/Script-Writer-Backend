package org.shivam.script_writer.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class OAuthController {

    @GetMapping("/oauth/profile")
    public Map<String, Object> googleProfile(
            @AuthenticationPrincipal OidcUser oidcUser
    ) {
        Map<String, Object> profile = new LinkedHashMap<>();

        profile.put("subject", oidcUser.getSubject());
        profile.put("name", oidcUser.getFullName());
        profile.put("email", oidcUser.getEmail());
        profile.put("emailVerified", oidcUser.getEmailVerified());

        return profile;
    }
}