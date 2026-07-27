package org.shivam.script_writer.service;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.shivam.script_writer.repo.UserRepository;

@Service
public class CustomOidcUserService
        implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final OidcUserService delegate = new OidcUserService();
    private final UserRepository userRepository;

    private final GoogleAccountService googleAccountService;

    public CustomOidcUserService(UserRepository userRepository, GoogleAccountService googleAccountService) {
        this.userRepository = userRepository;
        this.googleAccountService = googleAccountService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest)
            throws OAuth2AuthenticationException {

        OidcUser oidcUser = delegate.loadUser(userRequest);

        String subject = oidcUser.getSubject();
        String email = oidcUser.getEmail();
        boolean emailVerified =
                Boolean.TRUE.equals(oidcUser.getEmailVerified());

        if (subject == null || subject.isBlank()) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("invalid_user_info"),
                    "Google subject is missing"
            );
        }

        if (email == null || email.isBlank() || !emailVerified) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("invalid_user_info"),
                    "Google email is missing or unverified"
            );
        }

        boolean googleIdentityExists =
                userRepository.findByGoogleSubject(subject).isPresent();

        boolean emailAlreadyExists =
                userRepository.findByEmailIgnoreCase(email).isPresent();

        if (!googleIdentityExists && emailAlreadyExists) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("account_link_required"),
                    "An account already exists with this email"
            );
        }

        googleAccountService.findOrCreateUser(subject, email);


        return oidcUser;
    }
}