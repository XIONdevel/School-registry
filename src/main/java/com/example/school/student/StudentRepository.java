package com.example.school.student;

import com.example.school.group.Group;
import com.example.school.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByUser(User user);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    Optional<Student> findStudentByName(String name);

    Optional<Student> findStudentByEmail(String email);

    Optional<Student> findStudentByPhone(String phone);

    boolean existsStudentByPhoneAndIdNot(String phone, Long id);

    boolean existsStudentByEmailAndIdNot(String email, Long id);

    List<Student> findAllByGroup(Group group);
}
