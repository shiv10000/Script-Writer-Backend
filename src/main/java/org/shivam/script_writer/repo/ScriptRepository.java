package org.shivam.script_writer.repo;

import org.shivam.script_writer.entity.Script;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScriptRepository extends JpaRepository<Script, Long> {

}
