package org.shivam.script_writer.repo;

import org.shivam.script_writer.entity.ScriptCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScriptCategoryRepository extends JpaRepository<ScriptCategory,Long> {
}
