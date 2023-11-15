package com.example.demo.teacher;

import com.example.demo.exception.ExistsException;
import com.example.demo.group.Group;
import com.example.demo.group.GroupRepository;
import com.example.demo.subject.Subject;
import com.example.demo.subject.SubjectRepository;
import com.example.demo.utils.ServiceUtil;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private ServiceUtil serviceUtil;
    private TeacherService service;

    @BeforeEach
    void setUp() {
        service = new TeacherService(
                teacherRepository,
                subjectRepository,
                groupRepository,
                serviceUtil
        );
    }

    @Test
    void itShouldGetTeacherById() {
        //given
        Long id = 1L;
        Teacher teacher = new Teacher();
        teacher.setId(id);

        when(teacherRepository.findById(id))
                .thenReturn(Optional.of(teacher));

        //when
        Teacher recievedTeacher = service.getTeacher(id);

        //then
        assertThat(recievedTeacher).isEqualTo(teacher);
    }

    @Test
    void willThrowIfDidNotFoundTeacherById() {
        //given
        when(teacherRepository.findById(1L))
                .thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> service.getTeacher(1L))
                .hasMessageContaining("Teacher does not exists")
                .isInstanceOf(ExistsException.class);
    }

    @Test
    void willThrowIfGivenIdIsNullWhileGettingById() {
        //given
        Long id = null;
        //when
        //then
        assertThatThrownBy(() -> service.getTeacher(id))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("given id is null");
    }

    @Test
    void itShouldAddNewTeacher() {
        //given
        Long id = 1L;
        Teacher teacher = new Teacher();
        teacher.setId(id);
        //when
        service.addTeacher(teacher);
        //then
        verify(teacherRepository, times(1)).save(any(Teacher.class));
    }

    @Test
    void willThrowIfEmailIsTakenWhileAdding() {
        //given
        Long id = 1L;
        String email = "email@gmail.com";
        String phone = "+1234567890";
        Teacher teacher = new Teacher();
        teacher.setId(id);
        teacher.setEmail(email);
        teacher.setPhone(phone);

        when(teacherRepository.existsTeacherByEmailAndIdNot(email, id))
                .thenReturn(true);
        //when
        //then
        assertThatThrownBy(() -> service.addTeacher(teacher))
                .isInstanceOf(ExistsException.class)
                .hasMessageContaining("email is taken");
        verify(teacherRepository, times(0)).save(any(Teacher.class));
    }

    @Test
    void willThrowIfPhoneIsTakenWhileAdding() {
        //given
        Long id = 1L;
        String email = "email@gmail.com";
        String phone = "+1234567890";
        Teacher teacher = new Teacher();
        teacher.setId(id);
        teacher.setEmail(email);
        teacher.setPhone(phone);

        when(teacherRepository.existsTeacherByEmailAndIdNot(email, id))
                .thenReturn(false);

        when(teacherRepository.existsTeacherByPhoneAndIdNot(phone, id))
                .thenReturn(true);
        //when
        //then
        assertThatThrownBy(() -> service.addTeacher(teacher))
                .isInstanceOf(ExistsException.class)
                .hasMessageContaining("phone is taken");
        verify(teacherRepository, times(0)).save(any(Teacher.class));
    }

    @Test
    void willThrowIfGivenNullWhileAdding() {
        //given
        Long id = 1L;
        String email = "email@gmail.com";
        String phone = "+1234567890";
        Teacher teacher = null;
        
        //when
        //then
        assertThatThrownBy(() -> service.addTeacher(teacher))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("teacher can not be null");
        verify(teacherRepository, times(0)).save(any(Teacher.class));
    }

    @Test
    void itShouldDeleteTeacher() {
        //given
        Long id = 1L;
        Teacher teacher = new Teacher();
        teacher.setId(id);

        when(teacherRepository.findById(id))
                .thenReturn(Optional.of(teacher));
        //when
        service.deleteTeacher(id);
        //then
        verify(teacherRepository, times(1)).deleteById(id);
    }

    @Test
    void willThrowIfIdNullWhileDelete() {
        //given
        Long id = null;
        //when
        //then
        assertThatThrownBy(() -> service.deleteTeacher(id))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("id can not be null");
    }

    @Test
    void willThrowIfTeacherDoesNotExistsWhileDeleting() {
        //given
        Long id = 1L;
        when(teacherRepository.findById(id))
                .thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> service.deleteTeacher(id))
                .isInstanceOf(ExistsException.class)
                .hasMessageContaining("teacher with this id does not exists");
    }

    @Test
    void itShouldEditTeacher() {
        //given
        Long id = 1L;
        Teacher teacher = new Teacher();

        ArgumentCaptor<Teacher> teacherArgumentCaptor =
                ArgumentCaptor.forClass(Teacher.class);
        //when
        service.editTeacher(id, teacher);
        //then
        verify(teacherRepository, times(1))
                .save(teacherArgumentCaptor.capture());

        Teacher capturedTeacher = teacherArgumentCaptor.getValue();
        assertThat(teacher).isEqualTo(capturedTeacher);
    }

    @Test
    void willThrowIfIdOrTeacherIsNull() {
        //given
        Teacher teacher = null;
        Long id = null;
        //when
        //then
        assertThatThrownBy(() -> service.editTeacher(id, new Teacher()))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("id or teach is null");
        assertThatThrownBy(() -> service.editTeacher(1L, teacher))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("id or teach is null");
    }

    @Test
    void willThrowIfEmailIsTakenWhileEditing() {
        //given
        Long id = 1L;
        String email = "email@gmail.com";
        String phone = "+1234567890";
        Teacher teacher = new Teacher();
        teacher.setId(id);
        teacher.setEmail(email);
        teacher.setPhone(phone);

        when(teacherRepository.existsTeacherByEmailAndIdNot(email, id))
                .thenReturn(true);

        when(teacherRepository.existsTeacherByPhoneAndIdNot(phone, id))
                .thenReturn(false);
        //when
        //then
        assertThatThrownBy(() -> service.editTeacher(id, teacher))
                .isInstanceOf(ExistsException.class)
                .hasMessageContaining("email is taken");
        verify(teacherRepository, times(0)).save(any(Teacher.class));
    }

    @Test
    void willThrowIfPhoneIsTakenWhileEditing() {
        //given
        Long id = 1L;
        String email = "email@gmail.com";
        String phone = "+1234567890";
        Teacher teacher = new Teacher();
        teacher.setId(id);
        teacher.setEmail(email);
        teacher.setPhone(phone);

        when(teacherRepository.existsTeacherByPhoneAndIdNot(phone, id))
                .thenReturn(true);
        //when
        //then
        assertThatThrownBy(() -> service.editTeacher(id, teacher))
                .isInstanceOf(ExistsException.class)
                .hasMessageContaining("phone is taken");
        verify(teacherRepository, times(0)).save(any(Teacher.class));
    }

    @Test
    void itShouldAddSubject() {
        //given
        Long id = 1L;
        Teacher teacher = new Teacher();
        Subject subject = new Subject();

        when(teacherRepository.findById(id))
                .thenReturn(Optional.of(teacher));
        when(subjectRepository.findById(id))
                .thenReturn(Optional.of(subject));

        ArgumentCaptor<Subject> subjectCaptor =
                ArgumentCaptor.forClass(Subject.class);
        ArgumentCaptor<Teacher> teacherCaptor =
                ArgumentCaptor.forClass(Teacher.class);
        //when
        service.addSubject(id, id);
        //then
        verify(subjectRepository, times(1))
                .save(subjectCaptor.capture());
        verify(teacherRepository, times(1))
                .save(teacherCaptor.capture());

        Teacher capturedTeacher = teacherCaptor.getValue();
        Subject capturedSubject = subjectCaptor.getValue();

        assertThat(capturedTeacher.getSubjects().contains(capturedSubject))
                .isTrue();
        assertThat(capturedSubject.getTeachers().contains(capturedTeacher))
                .isTrue();
    }

    @Test
    void willThrowIfTeacherDoesNotExistsWhileAddingSubject() {
        //given
        when(teacherRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> service.addSubject(1L, 1L))
                .isInstanceOf(ExistsException.class)
                .hasMessageContaining("teacher with this id does not exists");
    }

    @Test
    void willThrowIfSubjectDoesNotExistsWhileAddingSubject() {
        //given
        when(teacherRepository.findById(anyLong()))
                .thenReturn(Optional.of(new Teacher()));
        when(subjectRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> service.addSubject(1L, 1L))
                .isInstanceOf(ExistsException.class)
                .hasMessageContaining("subject with this id does not exists");
    }

    @Test
    void itShouldRemoveSubject() {
        //given
        Long id = 1L;
        Teacher teacher = new Teacher();
        Subject subject = new Subject();
        teacher.addSubject(subject);
        subject.addTeacher(teacher);

        when(teacherRepository.findById(id))
                .thenReturn(Optional.of(teacher));
        when(subjectRepository.findById(id))
                .thenReturn(Optional.of(subject));

        ArgumentCaptor<Subject> subjectCaptor =
                ArgumentCaptor.forClass(Subject.class);
        ArgumentCaptor<Teacher> teacherCaptor =
                ArgumentCaptor.forClass(Teacher.class);
        //when
        service.removeSubject(id, id);
        //then
        verify(subjectRepository, times(1))
                .save(subjectCaptor.capture());
        verify(teacherRepository, times(1))
                .save(teacherCaptor.capture());

        Teacher capturedTeacher = teacherCaptor.getValue();
        Subject capturedSubject = subjectCaptor.getValue();

        assertThat(capturedTeacher.getSubjects().contains(capturedSubject))
                .isFalse();
        assertThat(capturedSubject.getTeachers().contains(capturedTeacher))
                .isFalse();
    }

    @Test
    void willThrowIfTeacherDoesNotExistsWhileRemovingSubject() {
        //given
        when(teacherRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> service.addSubject(1L, 1L))
                .isInstanceOf(ExistsException.class)
                .hasMessageContaining("teacher with this id does not exists");
    }

    @Test
    void willThrowIfSubjectDoesNotExistsWhileRemovingSubject() {
        //given
        when(teacherRepository.findById(anyLong()))
                .thenReturn(Optional.of(new Teacher()));
        when(subjectRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> service.addSubject(1L, 1L))
                .isInstanceOf(ExistsException.class)
                .hasMessageContaining("subject with this id does not exists");
    }

    @Test
    void itShouldAddGroup() {
        //given
        Long id = 1L;
        Group group = new Group();
        Teacher teacher = new Teacher();

        when(groupRepository.findById(id))
                .thenReturn(Optional.of(group));
        when(teacherRepository.findById(id))
                .thenReturn(Optional.of(teacher));

        ArgumentCaptor<Group> groupArgumentCaptor =
                ArgumentCaptor.forClass(Group.class);
        ArgumentCaptor<Teacher> teacherArgumentCaptor =
                ArgumentCaptor.forClass(Teacher.class);
        //when
        service.addGroup(id, id);
        //then
        verify(teacherRepository, times(1))
                .save(teacherArgumentCaptor.capture());
        verify(groupRepository, times(1))
                .save(groupArgumentCaptor.capture());

        Group capturedGroup = groupArgumentCaptor.getValue();
        Teacher capturedTeacher = teacherArgumentCaptor.getValue();

        assertThat(capturedGroup.getTeacherLead())
                .isEqualTo(capturedTeacher);
        assertThat(capturedTeacher.getGroup())
                .isEqualTo(capturedGroup);
    }

    @Test
    void willThrowIfTeacherEmptyWhileAddingGroup() {
        //given
        Long id = 1L;
        when(teacherRepository.findById(id))
                .thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> service.addGroup(id, id))
                .isInstanceOf(ExistsException.class)
                .hasMessageContaining("teacher with this id does not exists");
    }

    @Test
    void willThrowIfGroupEmptyWhileAddingGroup() {
        //given
        Long id = 1L;
        when(teacherRepository.findById(id))
                .thenReturn(Optional.of(new Teacher()));
        when(groupRepository.findById(id))
                .thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> service.addGroup(id, id))
                .isInstanceOf(ExistsException.class)
                .hasMessageContaining("group with this id does not exists");
    }

    @Test
    void itShouldRemoveGroup() {
        //given
        Long id = 1L;
        Group group = new Group();
        Teacher teacher = new Teacher();
        group.addTeacherLead(teacher);
        teacher.addGroup(group);

        when(groupRepository.findById(id))
                .thenReturn(Optional.of(group));
        when(teacherRepository.findById(id))
                .thenReturn(Optional.of(teacher));

        ArgumentCaptor<Group> groupArgumentCaptor =
                ArgumentCaptor.forClass(Group.class);
        ArgumentCaptor<Teacher> teacherArgumentCaptor =
                ArgumentCaptor.forClass(Teacher.class);
        //when
        service.removeGroup(id, id);
        //then
        verify(teacherRepository, times(1))
                .save(teacherArgumentCaptor.capture());
        verify(groupRepository, times(1))
                .save(groupArgumentCaptor.capture());

        Group capturedGroup = groupArgumentCaptor.getValue();
        Teacher capturedTeacher = teacherArgumentCaptor.getValue();

        assertThat(capturedTeacher.getGroup()).isNull();
        assertThat(capturedGroup.getTeacherLead()).isNull();
    }

    @Test
    void willThrowIfTeacherEmptyWhileRemovingGroup() {
        //given
        Long id = 1L;
        when(teacherRepository.findById(id))
                .thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> service.removeGroup(id, id))
                .isInstanceOf(ExistsException.class)
                .hasMessageContaining("teacher with this id does not exists");
    }

    @Test
    void willThrowIfGroupEmptyWhileRemovingGroup() {
        //given
        Long id = 1L;
        when(teacherRepository.findById(id))
                .thenReturn(Optional.of(new Teacher()));
        when(groupRepository.findById(id))
                .thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> service.removeGroup(id, id))
                .isInstanceOf(ExistsException.class)
                .hasMessageContaining("group with this id does not exists");
    }
}