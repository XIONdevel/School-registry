package com.example.demo.grade;

import com.example.demo.student.StudentRepository;
import com.example.demo.subject.SubjectRepository;
import com.example.demo.teacher.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class GradeServiceTest {

    @Mock
    private GradeRepository gradeRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private TeacherRepository teacherRepository;
    private GradeService service;

    @BeforeEach
    void setUp() {
        service = new GradeService (
                gradeRepository,
                studentRepository,
                subjectRepository,
                teacherRepository
        );
    }















}