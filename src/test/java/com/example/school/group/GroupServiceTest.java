package com.example.school.group;

import com.example.school.entity.group.Group;
import com.example.school.entity.group.GroupRepository;
import com.example.school.entity.group.GroupService;
import com.example.school.exception.ExistsException;
import com.example.school.exception.GroupNotFoundException;
import com.example.school.exception.TeacherNotFoundException;
import com.example.school.entity.student.StudentRepository;
import com.example.school.entity.teacher.Teacher;
import com.example.school.entity.teacher.TeacherRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class GroupServiceTest {
    @Mock
    private static GroupRepository groupRepository;
    @Mock
    private static TeacherRepository teacherRepository;
    @Mock
    private static StudentRepository studentRepository;
    private GroupService service;

    @BeforeEach
    void setUp() {
        service = new GroupService(
                groupRepository,
                teacherRepository,
                studentRepository
        );
    }

    @Test
    void itShouldGetGroupById() {
        //given
        Long id = 1L;
        Group group = new Group();

        when(groupRepository.findById(id))
                .thenReturn(Optional.of(group));
        //when
        Group received = service.getGroup(id);
        //then
        assertThat(received).isEqualTo(group);
    }

    @Test
    void itShouldAddNewGroup() {
        //given
        Group group = new Group();
        group.setName("1A");
        //when
        service.addNewGroup(group);
        //then
        verify(groupRepository, times(1))
                .save(any(Group.class));
    }

    @Test
    void willThrowIfNameIsEmpty() {
        //given
        Group group = new Group();
        group.setName(new String());
        //when
        //then
        assertThatThrownBy(() -> service.addNewGroup(group))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Group name can not be empty.");
    }

    @Test
    void itShouldRemoveTeacherFromGroup() {
        //given
        Long id = 1L;
        Teacher teacher = new Teacher();
        Group group = new Group();
        group.addTeacherLead(teacher);
        teacher.setMainGroup(group);

        when(groupRepository.findById(id))
                .thenReturn(Optional.of(group));
        when(teacherRepository.findById(id))
                .thenReturn(Optional.of(teacher));

        ArgumentCaptor<Teacher> teacherArgumentCaptor =
                ArgumentCaptor.forClass(Teacher.class);
        ArgumentCaptor<Group> groupArgumentCaptor =
                ArgumentCaptor.forClass(Group.class);
        //when
        service.removeTeacher(id, id);
        //then
        verify(teacherRepository, times(1))
                .save(teacherArgumentCaptor.capture());
        verify(groupRepository, times(1))
                .save(groupArgumentCaptor.capture());

        Teacher capturedTeacher = teacherArgumentCaptor.getValue();
        Group capturedGroup = groupArgumentCaptor.getValue();

        assertThat(capturedTeacher.getMainGroup()).isNull();
        assertThat(capturedGroup.getTeacherLead()).isNull();
    }

    @Test
    void willThrowIfTeacherIsEmptyWhileRemovingTeacher() {
        //given
        when(teacherRepository.findById(1L))
                .thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> service.removeTeacher(1L, 1L))
                .isInstanceOf(TeacherNotFoundException.class)
                .hasMessageContaining("Teacher with this id not found.");
    }

    @Test
    void itShouldAddTeacherToGroup() {
        //given
        Long id = 1L;
        Teacher teacher = new Teacher();
        Group group = new Group();

        when(groupRepository.findById(id))
                .thenReturn(Optional.of(group));
        when(teacherRepository.findById(id))
                .thenReturn(Optional.of(teacher));

        ArgumentCaptor<Teacher> teacherArgumentCaptor =
                ArgumentCaptor.forClass(Teacher.class);
        ArgumentCaptor<Group> groupArgumentCaptor =
                ArgumentCaptor.forClass(Group.class);
        //when
        service.addTeacher(id, id);
        //then
        verify(teacherRepository, times(1))
                .save(teacherArgumentCaptor.capture());
        verify(groupRepository, times(1))
                .save(groupArgumentCaptor.capture());

        Teacher capturedTeacher = teacherArgumentCaptor.getValue();
        Group capturedGroup = groupArgumentCaptor.getValue();

        assertThat(capturedTeacher.getMainGroup())
                .isEqualTo(capturedGroup);
        assertThat(capturedGroup.getTeacherLead())
                .isEqualTo(capturedTeacher);
    }

    @Test
    void willThrowIfTeacherIsEmptyWhileAddingTeacher() {
        //given
        when(teacherRepository.findById(1L))
                .thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> service
                .addTeacher(1L, 1L))
                .isInstanceOf(TeacherNotFoundException.class)
                .hasMessageContaining("Teacher with this id not found.");
    }

    @Test
    void itShouldDeleteGroup() {
        //given
        Long id = 1L;
        Group group = new Group();

        when(groupRepository.findById(id))
                .thenReturn(Optional.of(group));
        //when
        service.deleteGroup(id);
        //then
        verify(groupRepository, times(1)).deleteById(id);
    }

    @Test
    void itShouldEditGroup() {
        //given
        Long id = 1L;
        Group group = new Group();
        group.setId(id);
        group.setName("Math");

        when(groupRepository.existsById(anyLong()))
                .thenReturn(true);
        when(groupRepository.existsGroupByNameAndIdNot(anyString(), anyLong()))
                .thenReturn(false);
        ArgumentCaptor<Group> captor =
                ArgumentCaptor.forClass(Group.class);
        //when
        service.editGroup(id, group);
        //then
        verify(groupRepository, times(1)).save(captor.capture());
        Group captured = captor.getValue();
        assertThat(captured.getName()).isEqualTo("Math");
    }


    @Test
    void willThrowIfNameIsTakenWhileEditing() {
        //given
        Long id = 1L;
        Group group = new Group();
        group.setName("Math");
        group.setId(id);
        when(groupRepository.existsById(id))
                .thenReturn(true);
        when(groupRepository.existsGroupByNameAndIdNot(group.getName(), id))
                .thenReturn(true);
        //when
        //then
        assertThatThrownBy(() -> service.editGroup(id, group))
                .isInstanceOf(ExistsException.class)
                .hasMessageContaining("Name is taken.");
    }
}











