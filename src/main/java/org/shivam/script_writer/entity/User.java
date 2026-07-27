package org.shivam.script_writer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.shivam.script_writer.util.AccountStatus;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @Column(unique = true)
    String email;

    @Column(name = "google_subject", unique = true)
    private String googleSubject;

    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.UNVERIFIED;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_category_id", nullable = false)
    UserCategory userCategory;

    @OneToMany(mappedBy = "user")
    private List<Script> scripts = new ArrayList<>();


    public User(String name, String email, String password, UserCategory userCategory) {
        this.name = name;
        this.email  = email;
        this.passwordHash = password;
        this.userCategory = userCategory;
    }



    public static User createGoogleUser(
            String username,
            String email,
            String googleSubject,
            UserCategory userCategory
    ) {
        User user = new User();

        user.setName(username);
        user.setEmail(email);
        user.setGoogleSubject(googleSubject);
        user.setPasswordHash(null);
        user.setUserCategory(userCategory);

        return user;
    }
}
