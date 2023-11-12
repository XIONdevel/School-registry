package com.example.demo.group;

import com.example.demo.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {

    Boolean existsByName(String name);

}
