package org.shivam.script_writer.repo;

import org.shivam.script_writer.entity.ScriptCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScriptCategoryRepository extends JpaRepository<ScriptCategory,Long> {
}
