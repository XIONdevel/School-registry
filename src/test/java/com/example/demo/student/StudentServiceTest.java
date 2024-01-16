package com.example.demo.student;

import com.example.demo.exception.*;
import com.example.demo.parent.Parent;
import com.example.demo.parent.ParentRepository;
import com.example.demo.teacher.TeacherRepository;
import com.example.demo.user.permission.Role;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import com.example.demo.utils.ServiceUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private ParentRepository parentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ServiceUtil serviceUtil;
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentRepository,
                parentRepository,
                teacherRepository,
                userRepository,
                serviceUtil);
    }

    @Test
    void itShouldAddNewStudent() {
        //given
        Long id = 1L;
        Student student = new Student();
        student.setEmail("newemail@gmail.com");
        User user = new User();
        user.setRole(Role.STUDENT);

        when(serviceUtil.existsById(id)).thenReturn(false);
        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));

        //when
        studentService.createStudent(student, id);

        //then
        ArgumentCaptor<Student> studentArgumentCaptor =
                ArgumentCaptor.forClass(Student.class);

        verify(studentRepository).save(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void testEditStudent() {
        // given
        Long studentId = 1L;
        Student existingStudent = new Student();
        existingStudent.setId(studentId);
        existingStudent.setPhone("+1234567890");
        existingStudent.setEmail("existing@email.com");

        Student updatedStudent = new Student();
        updatedStudent.setId(studentId);
        updatedStudent.setPhone("+9876543210");
        updatedStudent.setEmail("new@email.com");

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        studentService.editStudent(studentId, updatedStudent);

        // then
        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, times(1)).save(existingStudent);

        assertThat(updatedStudent.getName()).isEqualTo(existingStudent.getName());
        assertThat(updatedStudent.getSurname()).isEqualTo(existingStudent.getSurname());
        assertThat(updatedStudent.getPhone()).isEqualTo(existingStudent.getPhone());
        assertThat(updatedStudent.getEmail()).isEqualTo(existingStudent.getEmail());
    }

    @Test
    void itShouldThrowWhenEmailTakenWhileEdit() {
        //given
        Long studentId = 1L;
        Student existingStudent = new Student();
        existingStudent.setId(studentId);
        existingStudent.setPhone("+1234567890");
        existingStudent.setEmail("existing@email.com");

        Student updatedStudent = new Student();
        updatedStudent.setId(studentId);
        updatedStudent.setPhone("+9876543210");
        updatedStudent.setEmail("new@email.com");

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));
        when(studentService.isEmailTaken(updatedStudent.getEmail(), studentId))
                .thenReturn(true);

        //when
        //then
        assertThatThrownBy(
                () -> studentService.editStudent(studentId, updatedStudent))
                .isInstanceOf(EmailTakenException.class)
                .hasMessageContaining("Email is taken.");
    }

    @Test
    void itShouldThrowWhenPhoneTakenWhileEdit() {
        //given
        Long studentId = 1L;
        Student existingStudent = new Student();
        existingStudent.setId(studentId);
        existingStudent.setPhone("+1234567890");
        existingStudent.setEmail("existing@email.com");

        Student updatedStudent = new Student();
        updatedStudent.setId(studentId);
        updatedStudent.setPhone("+9876543210");
        updatedStudent.setEmail("new@email.com");

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));
        when(studentService.isPhoneTaken(updatedStudent.getPhone(), studentId))
                .thenReturn(true);

        //when
        //then
        assertThatThrownBy(
                () -> studentService.editStudent(studentId, updatedStudent))
                .isInstanceOf(PhoneTakenException.class)
                .hasMessageContaining("Phone is taken.");
    }

    @Test
    void itShouldCheckIfThrowWhenNullWhileEdit() {
        //given
        Student updatedStudent = null;
        Long id = null;
        //when
        //then
        assertThatThrownBy(
                () -> studentService.editStudent(null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("id or updated student is null");
    }

    @Test
    void itShouldGetAllStudents() {
        //given
        when(studentRepository.findAll())
                .thenReturn(List.of(new Student(), new Student()));
        //when
        List<Student> students = studentService.getAll();
        //then
        assertThat(students.size()).isEqualTo(2);
    }

    @Test
    void itShouldGetStudentById() {
        //given
        Long id = 1L;
        Student student = new Student();
        student.setId(id);
        when(studentRepository.findById(id)).thenReturn(Optional.of(student));
        //when
        Student received = studentService.getStudent(id);
        //then
        assertThat(received).isEqualTo(student);
    }

    @Test
    void willItThrowIfNotExistsWhileGet() {
        //given
        Long id = 1L;
        when(studentRepository.findById(id))
                .thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> studentService.getStudent(id))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with given id not found.");
    }

    @Test
    void itShouldDeleteStudent() {
        //given
        Long id = 1L;
        Student student = new Student();
        student.setName("xion");
        student.setId(id);

        when(studentRepository.findById(id))
                .thenReturn(Optional.of(student));

        //when
        studentService.deleteStudent(id);
        //then
        verify(studentRepository, times(1)).deleteById(id);
    }

    @Test
    void willThrowNullWhileDeletingStudent() {
        //given
        Long id = null;
        //when
        //then
        assertThatThrownBy(() -> studentService.deleteStudent(id))
                .hasMessageContaining("id can not be null")
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void itShouldThrowNullWhileDeleteParentFromStudent() {
        //given
        Long parentId = null;
        Long studentId = null;
        //when
        //then
        assertThatThrownBy(
                () -> studentService.deleteParent(parentId, studentId))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Some of given id is null.");
    }

    @Test
    void willThrowIfParentDoesNotExists() {
        //given
        Long id = 1L;
        Student student = new Student();
        student.setId(id);
        Parent parent = new Parent();
        parent.setId(id);

        when(parentRepository.findById(id))
                .thenReturn(Optional.empty());
        when(studentRepository.findById(id))
                .thenReturn(Optional.of(student));

        //when
        //then
        assertThatThrownBy(() -> studentService.deleteParent(id, id))
                .isInstanceOf(ParentNotFoundException.class)
                .hasMessageContaining("Parent with given id not found.");
    }

    @Test
    void willThrowIfStudentDoesNotExists() {
        //given
        Long id = 1L;
        Student student = new Student();
        student.setId(id);
        Parent parent = new Parent();
        parent.setId(id);

        when(parentRepository.findById(id))
                .thenReturn(Optional.of(parent));
        when(studentRepository.findById(id))
                .thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> studentService.deleteParent(id, id))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with given id not found.");
    }

    @Test
    void shouldAddParentToStudent() {
        //given
        Long id = 1L;
        Parent parent = new Parent();
        parent.setId(id);
        Student student = new Student();
        student.setId(id);

        when(parentRepository.findById(id))
                .thenReturn(Optional.of(parent));
        when(studentRepository.findById(id))
                .thenReturn(Optional.of(student));
        //when
        studentService.addParent(id, id);
        //then
        verify(studentRepository, times(1)).save(any(Student.class));
        verify(parentRepository, times(1)).save(any(Parent.class));
    }

    @Test
    void willThrowIfStudentEmpty() {
        //given
        Long id = 1L;
        Parent parent = new Parent();
        parent.setId(id);
        Student student = new Student();
        student.setId(id);

        when(parentRepository.findById(id))
                .thenReturn(Optional.of(parent));
        when(studentRepository.findById(id))
                .thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> studentService.addParent(id, id))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with given id not found.");
        verify(studentRepository, times(0)).save(any(Student.class));
        verify(parentRepository, times(0)).save(any(Parent.class));
    }

    @Test
    void willThrowIfParentEmpty() {
        //given
        Long id = 1L;
        Parent parent = new Parent();
        parent.setId(id);
        Student student = new Student();
        student.setId(id);

        when(parentRepository.findById(id))
                .thenReturn(Optional.empty());
        when(studentRepository.findById(id))
                .thenReturn(Optional.of(student));
        //when
        //then
        assertThatThrownBy(() -> studentService.addParent(id, id))
                .isInstanceOf(ParentNotFoundException.class)
                .hasMessageContaining("Parent with given id not found.");
        verify(studentRepository, times(0)).save(any(Student.class));
        verify(parentRepository, times(0)).save(any(Parent.class));
    }
}

