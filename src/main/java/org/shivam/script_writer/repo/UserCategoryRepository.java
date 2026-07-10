package org.shivam.script_writer.repo;

import org.shivam.script_writer.entity.User;
import org.shivam.script_writer.entity.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserCategoryRepository extends JpaRepository<UserCategory,Long> {
    Optional<UserCategory> findByName(String name);


    List<UserCategory> getAllByName(String name);

}
