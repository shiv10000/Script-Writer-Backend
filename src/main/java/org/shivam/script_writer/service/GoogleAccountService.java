package org.shivam.script_writer.service;

import org.shivam.script_writer.entity.User;
import org.shivam.script_writer.entity.UserCategory;
import org.shivam.script_writer.repo.UserCategoryRepository;
import org.shivam.script_writer.repo.UserRepository;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;

@Service
public class GoogleAccountService {

    private static final String DEFAULT_USER_CATEGORY = "USER";

    private final UserRepository userRepository;
    private final UserCategoryRepository userCategoryRepository;

    public GoogleAccountService(
            UserRepository userRepository,
            UserCategoryRepository userCategoryRepository
    ) {
        this.userRepository = userRepository;
        this.userCategoryRepository = userCategoryRepository;
    }

    @Transactional
    public User findOrCreateUser(String subject, String email) {

        Optional<User> existingGoogleUser =
                userRepository.findByGoogleSubject(subject);

        if (existingGoogleUser.isPresent()) {
            return existingGoogleUser.get();
        }

        String normalizedEmail =
                email.trim().toLowerCase(Locale.ROOT);

        if (userRepository.findByEmailIgnoreCase(normalizedEmail).isPresent()) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("account_link_required"),
                    "An account already exists with this email"
            );
        }

        UserCategory userCategory =
                userCategoryRepository.findByName(DEFAULT_USER_CATEGORY)
                        .orElseGet(() -> userCategoryRepository.save(
                                new UserCategory(DEFAULT_USER_CATEGORY)
                        ));

        String displayName = normalizedEmail.substring(0, normalizedEmail.indexOf('@'));

        User googleUser = User.createGoogleUser(
                displayName,
                normalizedEmail,
                subject,
                userCategory
        );

        return userRepository.save(googleUser);
    }
}
