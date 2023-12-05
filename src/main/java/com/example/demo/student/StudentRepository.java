package com.example.demo.student;

import com.example.demo.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByPhone(String phone);

    List<Student> findAllByGroup(Group group);

    boolean existsByEmail(String email);

    Optional<Student> findStudentByName(String name);

    Optional<Student> findStudentByEmail(String email);

    Optional<Student> findStudentByPhone(String phone);

    boolean existsStudentByPhoneAndIdNot(String phone, Long id);

    boolean existsStudentByEmailAndIdNot(String email, Long id);

    List<Student> findAllFromGroup(Long groupId);
}
