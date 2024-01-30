package com.example.school.grade;

import com.example.school.student.Student;
import com.example.school.student.StudentRepository;
import com.example.school.subject.Subject;
import com.example.school.subject.SubjectRepository;
import com.example.school.teacher.Teacher;
import com.example.school.teacher.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
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
        service = new GradeService(
                gradeRepository,
                studentRepository,
                subjectRepository,
                teacherRepository
        );
    }

    @Test
    void itShouldGetAllGradesForStudentByDate() {
        //given
        Long id = 1L;
        Student student = getNewStudent(id);
        student.setId(id);
        LocalDate date = LocalDate.now();
        List<Grade> grades = getNewGradesStd(student, 2, date);

        when(studentRepository.findById(id))
                .thenReturn(Optional.of(student));
        when(gradeRepository.findAllByStudentAndDate(student, date))
                .thenReturn(grades);

        //when
        List<Grade> receivedGrades = service.getForStudentByDate(id, date);

        //then
        assertThat(receivedGrades.size())
                .isEqualTo(grades.size());
    }

    @Test
    void isShouldCheckIsDateValidMethod() {
        //given
        LocalDate correctDate = LocalDate.now();
        LocalDate wrongDate = LocalDate.of(2024, 1, 1);
        //when
        //then
        assertThat(service.isDateValid(correctDate))
                .isTrue();
        assertThat(service.isDateValid(wrongDate))
                .isFalse();
        assertThat(service.isDateValid(null))
                .isFalse();
    }

    @Test
    void itShouldCheckIsGradeValidMethod() {
        //given
        Grade correctGrade = Grade.builder()
                .student(new Student())
                .teacher(new Teacher())
                .subject(new Subject())
                .date(LocalDate.now())
                .build();

        Grade wrongGrade = Grade.builder()
                .student(new Student())
                .build();
        //when
        //then
        assertThat(service.isGradeValid(wrongGrade)).isFalse();
        assertThat(service.isGradeValid(correctGrade)).isTrue();
    }

    @Test
    void itShouldGetAllGradesForStudentByDateRange() {
        //given
        Long id = 1L;
        Student student = getNewStudent(id);
        LocalDate date = LocalDate.of(2023, 1, 2);
        LocalDate startDate = LocalDate.of(2023, 1, 2);
        LocalDate endDate = LocalDate.of(2023, 1, 3);
        List<Grade> grades = getNewGradesStd(student, 5, date);

        when(studentRepository.findById(id))
                .thenReturn(Optional.of(student));
        when(gradeRepository
                .findAllByStudentAndDateRange(student, startDate, endDate))
                .thenReturn(grades);
        //when
        List<Grade> receivedGrades =
                service.getForStudentByDate(id, startDate, endDate);
        //then
        assertThat(grades.size()).isEqualTo(receivedGrades.size());
    }

    @Test
    void itShouldGetAllGradesByDate() {
        //given
        LocalDate date = LocalDate.now();
        List<Grade> grades = getNewGradesStd(new Student(), 2, date);

        when(gradeRepository.findAllByDate(date))
                .thenReturn(grades);
        //when
        List<Grade> receivedGrades = service.getAllByDate(date);
        //then
        assertThat(grades.size())
                .isEqualTo(receivedGrades.size());
    }

    @Test
    void itShouldGetAllGradesByDateRange() {
        //given
        LocalDate startDate = LocalDate.now();
        List<Grade> grades = getNewGradesStd(new Student(), 2, LocalDate.now());
        LocalDate endDate = LocalDate.now();

        when(gradeRepository.findAllByDateRange(startDate, endDate))
                .thenReturn(grades);
        //when
        List<Grade> receivedGrades = service.getAllByDateRange(startDate, endDate);
        //then
        assertThat(grades.size())
                .isEqualTo(receivedGrades.size());
    }

    @Test
    void itShouldGetAllGradesForStudentById() {
        //given
        Long id = 1L;
        Student student = getNewStudent(id);
        List<Grade> grades = getNewGradesStd(student, 2, LocalDate.now());

        when(studentRepository.findById(id))
                .thenReturn(Optional.of(student));
        when(gradeRepository.findAllByStudent(any(Student.class)))
                .thenReturn(grades);

        //when
        List<Grade> receivedGrades = service.getAllForStudent(id);

        //then
        assertThat(receivedGrades.size()).isEqualTo(grades.size());
    }

    @Test
    void itShouldGetAllGradesForStudentBySubject() {
        //given
        Long id = 1L;
        Student student = getNewStudent(id);
        Subject subject = new Subject();
        subject.setId(id);
        List<Grade> grades = getNewGradesStd(student, 2, LocalDate.now());

        when(studentRepository.findById(id))
                .thenReturn(Optional.of(student));
        when(subjectRepository.findById(id))
                .thenReturn(Optional.of(subject));
        when(gradeRepository.findAllByStudentAndSubject(any(), any()))
                .thenReturn(grades);

        //when
        List<Grade> receivedGrades = service.getAllForStudentBySubject(id, id);

        //then
        assertThat(receivedGrades.size()).isEqualTo(grades.size());
    }

    @Test
    void itShouldGetAllGradesForStudentBySubjectAndDate() {
        //given
        Long id = 1L;
        Student student = getNewStudent(id);
        Subject subject = new Subject();
        subject.setId(id);
        LocalDate date = LocalDate.now();
        List<Grade> grades = getNewGradesStd(student, 2, date);

        when(studentRepository.findById(id))
                .thenReturn(Optional.of(student));
        when(subjectRepository.findById(id))
                .thenReturn(Optional.of(subject));
        when(gradeRepository.findAllByStudentAndSubjectAndDate(
                student,
                subject,
                date
        )).thenReturn(grades);

        //when
        List<Grade> receivedGrades = service
                .getAllForStudentBySubjectAndDate(id, id, date);
        //then
        assertThat(grades.size()).isEqualTo(receivedGrades.size());
    }

    @Test
    void itShouldGetAllGradesForStudentBySubjectAndDateRange() {
        //given
        Long id = 1L;
        Student student = getNewStudent(id);
        Subject subject = new Subject();
        subject.setId(id);
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        List<Grade> grades = getNewGradesStd(student, 2, LocalDate.now());

        when(studentRepository.findById(id))
                .thenReturn(Optional.of(student));
        when(subjectRepository.findById(id))
                .thenReturn(Optional.of(subject));
        when(gradeRepository.findAllByStudentAndSubjectAndDateRange(
                student,
                subject,
                startDate,
                endDate
        )).thenReturn(grades);

        //when
        List<Grade> receivedGrades = service
                .getAllForStudentBySubjectAndDate(id, id, startDate, endDate);
        //then
        assertThat(grades.size()).isEqualTo(receivedGrades.size());
    }

    @Test
    void itShouldGetAllBySubject() {
        //given
        Long id = 1L;
        Subject subject = new Subject();
        List<Grade> grades = getNewGradesStd(new Student(), 2, LocalDate.now());

        when(subjectRepository.findById(id))
                .thenReturn(Optional.of(subject));
        when(gradeRepository.findAllBySubject(any(Subject.class)))
                .thenReturn(grades);
        //when
        List<Grade> receivedGrades = service.getAllBySubject(id);
        //then
        assertThat(grades.size()).isEqualTo(receivedGrades.size());
    }

    @Test
    void itShouldGetAllByTeacher() {
        //given
        Long id = 1L;
        Teacher teacher = new Teacher();
        List<Grade> grades = getNewGradesStd(new Student(), 2, LocalDate.now());

        when(teacherRepository.findById(id))
                .thenReturn(Optional.of(teacher));
        when(gradeRepository.findAllByTeacher(any(Teacher.class)))
                .thenReturn(grades);
        //when
        List<Grade> receivedGrades = service.getAllByTeacher(id);
        //then
        assertThat(grades.size()).isEqualTo(receivedGrades.size());
    }

    @Test
    void itShouldAddNewGrade() {
        //given
        Grade grade = getNewGradesStd(new Student(), 1, LocalDate.now()).get(0);
        //when
        service.addNewGrade(grade);
        //then
        verify(gradeRepository, times(1)).save(any(Grade.class));
    }

    private List<Grade> getNewGradesStd(
            Student student,
            int count,
            LocalDate date
    ) {
        List<Grade> grades = new ArrayList<>();

        for (long i = 0; i < count; i++) {
            Grade grade = new Grade();
            grade.setId(i);
            grade.setTeacher(new Teacher());
            grade.setSubject(new Subject());
            grade.setValue(1);
            grade.setStudent(student);
            grade.setDate(date);
            grades.add(grade);
        }
        return grades;
    }

    private Student getNewStudent(Long id) {
        Student student = new Student();
        student.setId(id);
        student.setName("testStudent");
        return student;
    }


}