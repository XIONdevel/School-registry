package com.example.demo.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Teacher findTeacherByEmail(@Param("email") String email);

//    @Query("SELECT s FROM Teacher s WHERE s.phone = :phone")
    Teacher findTeacherByPhone(@Param("phone") String phone);

    Boolean existsTeacherByEmail(@Param("Email") String email);

//    @Query("SELECT COUNT(s) FROM Teacher s WHERE s.phone=:phone")
    Boolean existsTeacherByPhone(@Param("phone") String phone);
}
