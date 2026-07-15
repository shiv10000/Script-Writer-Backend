package org.shivam.script_writer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    String email;
    private String passwordHash;

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
}
