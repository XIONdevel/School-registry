package com.example.demo.grade;

import com.example.demo.student.Student;
import com.example.demo.subject.Subject;
import com.example.demo.teacher.Teacher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "grade")
public class Grade {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private int value;

    @Column(nullable = false)
    private LocalDate date;

    @OneToOne
    @JoinColumn(name = "subject_id")
    @Column(nullable = false)
    private Subject subject;

    @OneToOne
    @JoinColumn(name = "student_id")
    @Column(nullable = false)
    private Student student;

    @OneToOne
    @JoinColumn(name = "teacher_id")
    @Column(nullable = false)
    private Teacher teacher;
}


















