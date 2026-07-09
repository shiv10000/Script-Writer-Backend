package org.shivam.script_writer.repo;


import org.shivam.script_writer.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {
}

