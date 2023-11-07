package com.example.demo.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByPhone(String phone);

    @Query("SELECT s FROM Student s WHERE s.group.id=:groupId")
    List<Student> findAllFromGroup(@Param(value = "groupId") Long groupId);

    boolean existsByEmail(String email);

}
