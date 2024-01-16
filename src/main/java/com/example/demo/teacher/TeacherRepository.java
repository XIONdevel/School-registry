package com.example.demo.teacher;

import com.example.demo.parent.Parent;
import com.example.demo.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByUser(User user);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsTeacherByPhoneAndIdNot(String phone, Long id);

    boolean existsTeacherByEmailAndIdNot(String email, Long id);
}
