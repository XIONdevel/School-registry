package com.example.demo.parent;

import com.example.demo.exception.ExistsException;
import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
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

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ParentServiceTest {
    @Mock
    private ParentRepository parentRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ServiceUtil serviceUtil;
    private ParentService service;

    @BeforeEach
    void setUp() {
        service = new ParentService(
                parentRepository,
                studentRepository,
                teacherRepository,
                userRepository,
                serviceUtil
        );
    }

    @Test
    void itShouldAddParent() {
        //given
        Long id = 1L;
        Parent parent = new Parent();
        parent.setEmail("email@gmail.com");
        parent.setPhone("+1243493");
        User user = new User();
        user.setRole(Role.PARENT);
        when(serviceUtil.isEmailTaken(anyString()))
                .thenReturn(false);
        when(serviceUtil.isPhoneTaken(anyString()))
                .thenReturn(false);
        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));
        //when
        service.createParent(parent, id);
        //then
        verify(parentRepository, times(1))
                .save(parent);
    }

    @Test
    void itShouldDeleteParent() {
        //given
        Long id = 1L;
        Parent parent = new Parent();
        parent.setId(id);
        when(parentRepository.findById(id))
                .thenReturn(Optional.of(parent));
        //when
        service.deleteParent(id);
        //then
        verify(parentRepository, times(1))
                .deleteById(id);
    }

    @Test
    void itShouldEditParent() {
        //given
        Long id = 1L;
        String phone = "+38423423";
        String email = "fqfwef@gfefs";
        Parent forUpdate = new Parent();
        forUpdate.setEmail(email);
        forUpdate.setPhone(phone);
        forUpdate.setId(id);

        when(parentRepository.findById(id))
                .thenReturn(Optional.of(forUpdate));
        when(service.isEmailTaken(email, id))
                .thenReturn(false);
        when(service.isPhoneTaken(phone, id))
                .thenReturn(false);

        ArgumentCaptor<Parent> parentArgumentCaptor =
                ArgumentCaptor.forClass(Parent.class);
        //when
        service.editParent(id, forUpdate);
        //then
        verify(parentRepository, times(1))
                .save(parentArgumentCaptor.capture());
        Parent captured = parentArgumentCaptor.getValue();

        assertThat(captured).isEqualTo(forUpdate);
    }

    @Test
    void willThrowIfEmailTakenWhileEditingParent() {
        //given
        Long id = 1L;
        Parent parent = new Parent();
        parent.setId(id);
        parent.setEmail("email@gmail.com");
        parent.setPhone("+1243493");

        when(parentRepository.findById(1L))
                .thenReturn(Optional.of(parent));
        when(service.isEmailTaken(parent.getEmail(), id))
                .thenReturn(true);
        //when
        //then
        assertThatThrownBy(() -> service.editParent(1L, parent))
                .isInstanceOf(ExistsException.class)
                .hasMessageContaining("email is taken");
    }

    @Test
    void willThrowIfPhoneTakenWhileEditingParent() {
        //given
        Long id = 1L;
        String email = "dasdsa@gmail.com";
        String phone = "+121241241";
        Parent parent = new Parent();
        parent.setId(id);
        parent.setEmail(email);
        parent.setPhone(phone);

        when(parentRepository.findById(id))
                .thenReturn(Optional.of(parent));
        when(service.isEmailTaken(email, id))
                .thenReturn(false);
        when(service.isPhoneTaken(phone, id))
                .thenReturn(true);
        //when
        //then
        assertThatThrownBy(() -> service.editParent(id, parent))
                .isInstanceOf(ExistsException.class)
                .hasMessageContaining("phone is taken");
    }

    @Test
    void itShouldAddChild() {
        //given
        Long id = 1L;
        Parent parent = new Parent();
        Student student = new Student();

        when(studentRepository.findById(id))
                .thenReturn(Optional.of(student));
        when(parentRepository.findById(id))
                .thenReturn(Optional.of(parent));

        //when
        service.addChild(id, id);
        //then
        verify(studentRepository, times(1))
                .save(any(Student.class));
        verify(parentRepository, times(1))
                .save(any(Parent.class));
    }

    @Test
    void willThrowIfStudentDoesNotExistsWhileAddingChild() {
        //given
        Long id = 1L;
        Student student = new Student();
        Parent parent = new Parent();
        when(studentRepository.findById(id))
                .thenReturn(Optional.empty());
        when(parentRepository.findById(id))
                .thenReturn(Optional.of(parent));
        //when
        //then
        assertThatThrownBy(() -> service.addChild(id, id))
                .isInstanceOf(ExistsException.class)
                .hasMessageContaining("student with given id does not exists");
    }

    @Test
    void willThrowIfParentDoesNotExistsWhileAddingChild() {
        //given
        Long id = 1L;
        Student student = new Student();
        Parent parent = new Parent();
        when(studentRepository.findById(id))
                .thenReturn(Optional.of(student));
        when(parentRepository.findById(id))
                .thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> service.addChild(id, id))
                .isInstanceOf(ExistsException.class)
                .hasMessageContaining("parent with given id does not exists");
    }

    @Test
    void willThrowIfStudentDoesNotExistsWhileRemovingChild() {
        //given
        Long id = 1L;
        Student student = new Student();
        Parent parent = new Parent();
        when(studentRepository.findById(id))
                .thenReturn(Optional.empty());
        when(parentRepository.findById(id))
                .thenReturn(Optional.of(parent));
        //when
        //then
        assertThatThrownBy(() -> service.removeChild(id, id))
                .isInstanceOf(ExistsException.class)
                .hasMessageContaining("student with this id does not exists");
    }

    @Test
    void willThrowIfParentDoesNotExistsWhileRemovingChild() {
        //given
        Long id = 1L;
        Student student = new Student();
        Parent parent = new Parent();
        when(studentRepository.findById(id))
                .thenReturn(Optional.of(student));
        when(parentRepository.findById(id))
                .thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> service.removeChild(id, id))
                .isInstanceOf(ExistsException.class)
                .hasMessageContaining("parent with this id does not exists");
    }



}