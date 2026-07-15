package org.shivam.script_writer.repo;

import org.shivam.script_writer.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @EntityGraph(attributePaths = "userCategory")
    Optional<User> findByName(String name);


    boolean existsByUserCategory_Name(String categoryName);
}
