package com.example.demo.grade;

import com.example.demo.student.Student;
import com.example.demo.subject.Subject;
import com.example.demo.teacher.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    List<Grade> findAllByStudent(Student student);

    List<Grade> findAllByStudentAndDate(Student student, LocalDate date);

    List<Grade> findAllByDate(LocalDate date);

    List<Grade> findAllByStudentAndSubjectAndDate(Student student, Subject subject, LocalDate date);

    List<Grade> findAllByStudentAndSubject(Student student, Subject subject);

    List<Grade> findAllBySubject(Subject subject);

    List<Grade> findAllByTeacher(Teacher teacher);

    @Query("SELECT g FROM Grade g WHERE g.student = :student AND g.date BETWEEN :startDate AND :endDate")
    List<Grade> findAllByStudentAndDateRange(
            @Param("student") Student student,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT g FROM Grade g WHERE g.date BETWEEN :startDate AND :endDate")
    List<Grade> findAllByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT g FROM Grade g WHERE g.student = :student AND g.subject = :subject AND g.date BETWEEN :startDate AND :endDate")
    List<Grade> findAllByStudentAndSubjectAndDateRange(
            @Param("student") Student student,
            @Param("subject") Subject subject,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


}
