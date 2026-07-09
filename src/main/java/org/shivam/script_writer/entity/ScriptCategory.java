package org.shivam.script_writer.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "script_category")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScriptCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", nullable = true)
    private User createdBy;


    @OneToMany(mappedBy = "category")
    List<Script> listOfScript = new ArrayList<>();
}
