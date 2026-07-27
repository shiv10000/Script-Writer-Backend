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

    @EntityGraph(attributePaths = "userCategory")
    Optional<User> findByGoogleSubject(String googleSubject);

    @EntityGraph(attributePaths = "userCategory")
    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByEmail(String email);


    boolean existsByEmailIgnoreCase(String email);






    boolean existsByUserCategory_Name(String categoryName);


}
