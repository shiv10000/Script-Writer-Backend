package org.shivam.script_writer.config;

import org.shivam.script_writer.entity.User;
import org.shivam.script_writer.entity.UserCategory;
import org.shivam.script_writer.repo.UserCategoryRepository;
import org.shivam.script_writer.repo.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.bootstrap-admin.enabled", havingValue = "true")
public class AdminBootstrap implements ApplicationRunner {

    private static final String ADMIN_CATEGORY = "ADMIN";

    private final UserRepository userRepository;
    private final UserCategoryRepository userCategoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminName;
    private final String adminEmail;
    private final String adminPassword;

    public AdminBootstrap(
            UserRepository userRepository,
            UserCategoryRepository userCategoryRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.bootstrap-admin.name:}") String adminName,
            @Value("${app.bootstrap-admin.email:}") String adminEmail,
            @Value("${app.bootstrap-admin.password:}") String adminPassword) {
        this.userRepository = userRepository;
        this.userCategoryRepository = userCategoryRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminName = adminName;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.existsByUserCategory_Name(ADMIN_CATEGORY)) {
            return;
        }

        validateBootstrapCredentials();

        if (userRepository.findByName(adminName).isPresent()) {
            throw new IllegalStateException("Cannot bootstrap admin: username already exists");
        }

        UserCategory adminCategory = userCategoryRepository.findByName(ADMIN_CATEGORY)
                .orElseGet(() -> userCategoryRepository.save(new UserCategory(ADMIN_CATEGORY)));

        userRepository.save(new User(
                adminName,
                adminEmail,
                passwordEncoder.encode(adminPassword),
                adminCategory
        ));
    }

    private void validateBootstrapCredentials() {
        if (adminName.isBlank() || adminEmail.isBlank() || adminPassword.isBlank()) {
            throw new IllegalStateException(
                    "Admin bootstrap is enabled, but name, email, or password is missing"
            );
        }
    }
}
