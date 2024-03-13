package com.example.school.entity.student;

import com.example.school.entity.group.Group;
import com.example.school.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByUser(User user);

    boolean existsByPhone(String phone);

    Optional<Student> findStudentByFirstname(String firstname);

    Optional<Student> findStudentByPhone(String phone);

    boolean existsStudentByPhoneAndIdNot(String phone, Long id);

//    boolean existsStudentByEmailAndIdNot(String email, Long id);

    List<Student> findAllByGroup(Group group);
}
