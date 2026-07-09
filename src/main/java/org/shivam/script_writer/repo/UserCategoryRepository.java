package org.shivam.script_writer.repo;

import org.shivam.script_writer.entity.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCategoryRepository extends JpaRepository<UserCategory,Long> {
}
