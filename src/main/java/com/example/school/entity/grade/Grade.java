package com.example.school.entity.grade;

import com.example.school.entity.student.Student;
import com.example.school.entity.subject.Subject;
import com.example.school.entity.teacher.Teacher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.sql.Date;
import java.time.LocalDate;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @Override
    public String toString() {
        return "Grade: " +
                "id=" + id +
                ", grade=" + value +
                ", date=" + date +
                ", subject id=" + subject.getId() +
                ", student id=" + student.getId() +
                ", teacher id=" + teacher.getId() +
                '}';
    }
}


















