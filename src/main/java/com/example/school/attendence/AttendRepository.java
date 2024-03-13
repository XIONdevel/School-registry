package com.example.school.attendence;

import com.example.school.dto.AttendanceDTO;
import com.example.school.entity.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendRepository extends JpaRepository<Attend, Long> {

    @Query("""
    SELECT NEW com.example.school.dto.AttendanceDTO
    (a.status, a.date, a.absentCause, stud.firstname, stud.lastname, sub.name)
    FROM Attend a
    JOIN a.student stud
    JOIN a.subject sub
    WHERE a.date =:date AND stud =:student
    """)
    List<AttendanceDTO> findAttendsByStudentAndDate(Student student, LocalDate date);













}
