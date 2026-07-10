package org.shivam.script_writer.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_category")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "userCategory")
    private List<User> users = new ArrayList<>();

    public UserCategory(String name) {
        this.name = name;
    }
}
