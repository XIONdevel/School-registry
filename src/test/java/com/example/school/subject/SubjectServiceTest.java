package com.example.school.subject;

import com.example.school.entity.subject.Subject;
import com.example.school.entity.subject.SubjectRepository;
import com.example.school.entity.subject.SubjectService;
import com.example.school.exception.NameTakenException;
import com.example.school.exception.SubjectNotFoundException;
import com.example.school.exception.TeacherNotFoundException;
import com.example.school.entity.teacher.Teacher;
import com.example.school.entity.teacher.TeacherRepository;
import com.example.school.utils.ServiceUtil;
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
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {
    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private ServiceUtil serviceUtil;
    private SubjectService service;

    @BeforeEach
    void setUp() {
        service = new SubjectService(
                subjectRepository,
                teacherRepository,
                serviceUtil
        );
    }

    @Test
    void shouldGetAllSubjects() {
        //given
        //when
        service.getAll();
        //then
        verify(subjectRepository, times(1)).findAll();
    }

    @Test
    void shouldGetSubjectById() {
        //given
        Long id = 1L;
        Subject subject = new Subject();
        subject.setId(id);
        subject.setName("Math");
        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));
        //when
        Subject received = service.getSubject(id);
        //then
        assertThat(received).isEqualTo(subject);
    }

    @Test
    void willThrowWhenDoesNotExistsWhileGetById() {
        //given
        when(subjectRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> service.getSubject(1L))
                .isInstanceOf(SubjectNotFoundException.class)
                .hasMessageContaining("Subject with given id not found.");
    }

    @Test
    void shouldAddNewSubject() {
        //given
        Subject subject = new Subject("Math");
        //when
        service.addSubject(subject);
        //then
        verify(subjectRepository, times(1)).save(any(Subject.class));
    }

    @Test
    void willThrowWhenSubjectNullWhileAdding() {
        //given
        Subject subject = null;
        //when
        //then
        assertThatThrownBy(() -> service.addSubject(subject))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Subject is null.");
    }

    @Test
    void willThrowIfNameISTaken() {
        //given
        Subject subject = new Subject("Math");
        subject.setId(1L);
        when(subjectRepository
                .existsSubjectByNameAndIdNot("Math", 1L))
                .thenReturn(true);
        //when
        //then
        assertThatThrownBy(() -> service.addSubject(subject))
                .isInstanceOf(NameTakenException.class)
                .hasMessageContaining("Name is taken.");
    }

    @Test
    void itShouldDeleteSubjectById() {
        //given
        Long id = 1L;
        Subject subject = new Subject();
        subject.setName("Math");

        when(subjectRepository.findById(id))
                .thenReturn(Optional.of(subject));

        //when
        service.deleteSubject(id);
        //then
        verify(subjectRepository, times(1)).deleteById(id);
    }

    @Test
    void willThrowIfIdNullWhileDeleting() {
        //given
        Long id = null;
        //when
        //then
        assertThatThrownBy(() -> service.deleteSubject(id))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Given id is null.");
        verify(subjectRepository, times(0)).deleteById(anyLong());
    }

    @Test
    void willThrowsIfSubjectEmptyWhileDeleting() {
        //given
        Long id = 1L;
        Subject subject = new Subject();
        subject.setName("Math");
        subject.setId(id);

        when(subjectRepository.findById(id))
                .thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> service.deleteSubject(id))
                .isInstanceOf(SubjectNotFoundException.class)
                .hasMessageContaining("Subject with given id not found.");
        verify(subjectRepository, times(0)).deleteById(anyLong());
    }

    @Test
    void willDeleteSubjectFromAllTeachers() {
        Long id = 1L;
        Subject subject = new Subject();
        subject.setName("Math");
        subject.setId(id);

        Teacher teacher1 = new Teacher();
        subject.addTeacher(teacher1);
        teacher1.addSubject(subject);

        Teacher teacher2 = new Teacher();
        subject.addTeacher(teacher2);
        teacher2.addSubject(subject);

        when(subjectRepository.findById(id))
                .thenReturn(Optional.of(subject));

        //when
        service.deleteSubject(id);
        //then
        verify(subjectRepository, times(1)).deleteById(id);
        assertThat(teacher1.getSubjects().size()).isEqualTo(0);
        assertThat(teacher2.getSubjects().size()).isEqualTo(0);
    }

    @Test
    void itShouldEditSubject() {
        //given
        Long id = 1L;
        Subject subject = new Subject();
        subject.setId(id);
        //when
        service.editSubject(id, subject);
        //then
        verify(subjectRepository, times(1)).save(any(Subject.class));
    }

    @Test
    void willThrowIfIdNull() {
        //given
        Long id = null;
        Subject subject = new Subject();
        //when
        //then
        assertThatThrownBy(() -> service.editSubject(id, subject))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Subject or id is null.");
    }

    @Test
    void willThrowIdSubjectIsNull() {
        //given
        Long id = 1L;
        Subject subject = null;
        //when
        //then
        assertThatThrownBy(() -> service.editSubject(id, subject))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Subject or id is null.");
        verify(subjectRepository, times(0)).save(subject);
    }

    @Test
    void willThrowIfNameIsTakenWhileEditing() {
        //given
        Long id = 1L;
        Subject subject = new Subject();
        subject.setName("Math");

        when(subjectRepository
                .existsSubjectByNameAndIdNot("Math", id))
                .thenReturn(true);
        //when
        //then
        assertThatThrownBy(() -> service.editSubject(id, subject))
                .isInstanceOf(NameTakenException.class)
                .hasMessageContaining("Name is taken.");
        verify(subjectRepository, times(0)).save(subject);
    }

    @Test
    void shouldAddTeacherToSubject() {
        //given
        Long id = 1L;

        Subject subject = new Subject();
        subject.setId(id);
        Teacher teacher = new Teacher();
        teacher.setId(id);

        when(teacherRepository.findById(id))
                .thenReturn(Optional.of(teacher));
        when(subjectRepository.findById(id))
                .thenReturn(Optional.of(subject));

        ArgumentCaptor<Subject> subjectArgumentCaptor =
                ArgumentCaptor.forClass(Subject.class);

        ArgumentCaptor<Teacher> teacherArgumentCaptor =
                ArgumentCaptor.forClass(Teacher.class);
        //when
        service.addTeacher(id, id);
        //then
        verify(subjectRepository, times(1))
                .save(subjectArgumentCaptor.capture());
        verify(teacherRepository, times(1))
                .save(teacherArgumentCaptor.capture());

        Subject capturedSubject = subjectArgumentCaptor.getValue();
        Teacher capturedTeacher = teacherArgumentCaptor.getValue();

        assertThat(capturedSubject.getTeachers().size())
                .isEqualTo(1);
        assertThat(capturedTeacher.getSubjects().size())
                .isEqualTo(1);

        assertThat(capturedSubject).isEqualTo(subject);
        assertThat(capturedTeacher).isEqualTo(teacher);
    }

    @Test
    void willThrowIfTeacherDoesNotExistsWhileAddingTeacher() {
        //given
        Long id = 1L;

        Subject subject = new Subject();
        subject.setId(id);
        Teacher teacher = new Teacher();
        teacher.setId(id);

        when(teacherRepository.findById(id))
                .thenReturn(Optional.empty());
        when(subjectRepository.findById(id))
                .thenReturn(Optional.of(subject));
        //when
        //then
        assertThatThrownBy(() -> service.addTeacher(id, id))
                .isInstanceOf(TeacherNotFoundException.class)
                .hasMessageContaining("Teacher with this id not found.");
        verify(subjectRepository, times(0)).save(any(Subject.class));
        verify(teacherRepository, times(0)).save(any(Teacher.class));
    }

    @Test
    void willThrowIfSubjectDoesNotExistsWhileAddingTeacher() {
        //given
        Long id = 1L;

        Subject subject = new Subject();
        subject.setId(id);
        Teacher teacher = new Teacher();
        teacher.setId(id);

        when(teacherRepository.findById(id))
                .thenReturn(Optional.of(teacher));
        when(subjectRepository.findById(id))
                .thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> service.addTeacher(id, id))
                .isInstanceOf(SubjectNotFoundException.class)
                .hasMessageContaining("Subject with given id not found.");
        verify(subjectRepository, times(0)).save(any(Subject.class));
        verify(teacherRepository, times(0)).save(any(Teacher.class));
    }

    @Test
    void itShouldRemoveTeacherFromSubject() {
        //given
        Long id = 1L;

        Subject subject = new Subject();
        subject.setId(id);
        Teacher teacher = new Teacher();
        teacher.setId(id);

        subject.addTeacher(teacher);
        teacher.addSubject(subject);

        when(teacherRepository.findById(id))
                .thenReturn(Optional.of(teacher));
        when(subjectRepository.findById(id))
                .thenReturn(Optional.of(subject));

        ArgumentCaptor<Subject> subjectArgumentCaptor =
                ArgumentCaptor.forClass(Subject.class);
        ArgumentCaptor<Teacher> teacherArgumentCaptor =
                ArgumentCaptor.forClass(Teacher.class);
        //when
        service.removeTeacher(id, id);
        //then
        verify(teacherRepository, times(1))
                .save(teacherArgumentCaptor.capture());
        verify(subjectRepository, times(1))
                .save(subjectArgumentCaptor.capture());

        Subject capturedSubject = subjectArgumentCaptor.getValue();
        Teacher capturedTeacher = teacherArgumentCaptor.getValue();

        assertThat(capturedSubject.getTeachers().size())
                .isEqualTo(0);
        assertThat(capturedTeacher.getSubjects().size())
                .isEqualTo(0);

        assertThat(capturedSubject).isEqualTo(subject);
        assertThat(capturedTeacher).isEqualTo(teacher);
    }

    @Test
    void willThrowIfTeacherIsEmptyWhileRemovingTeacher() {
        //given
        Long id = 1L;

        Subject subject = new Subject();
        subject.setId(id);
        Teacher teacher = new Teacher();
        teacher.setId(id);

        when(teacherRepository.findById(id))
                .thenReturn(Optional.empty());
        when(subjectRepository.findById(id))
                .thenReturn(Optional.of(subject));
        //when
        //then
        assertThatThrownBy(() -> service.removeTeacher(id, id))
                .isInstanceOf(TeacherNotFoundException.class)
                .hasMessageContaining("Teacher with this id not found.");
        verify(subjectRepository, times(0)).save(any(Subject.class));
        verify(teacherRepository, times(0)).save(any(Teacher.class));
    }

    @Test
    void willThrowIfSubjectIsEmptyWhileRemovingTeacher() {
        //given
        Long id = 1L;

        Subject subject = new Subject();
        subject.setId(id);
        Teacher teacher = new Teacher();
        teacher.setId(id);

        when(teacherRepository.findById(id))
                .thenReturn(Optional.of(teacher));
        when(subjectRepository.findById(id))
                .thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> service.addTeacher(id, id))
                .isInstanceOf(SubjectNotFoundException.class)
                .hasMessageContaining("Subject with given id not found.");
        verify(subjectRepository, times(0)).save(any(Subject.class));
        verify(teacherRepository, times(0)).save(any(Teacher.class));
    }
}


















