package com.example.school.entity.teacher;

import com.example.school.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByUser(User user);

    boolean existsByPhone(String phone);

    boolean existsTeacherByPhoneAndIdNot(String phone, Long id);

}
