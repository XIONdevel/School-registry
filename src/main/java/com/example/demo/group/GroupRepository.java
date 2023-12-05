package com.example.demo.group;

import com.example.demo.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    boolean existsByName(String name);

    boolean existsGroupByNameAndIdNot(String name, Long id);

    Optional<Group> findByName(String name);

}
