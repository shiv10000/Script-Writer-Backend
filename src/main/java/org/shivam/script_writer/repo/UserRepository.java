package org.shivam.script_writer.repo;

import org.shivam.script_writer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
