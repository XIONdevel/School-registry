package com.example.school.attendence;

import com.example.school.dto.AttendanceDTO;
import com.example.school.exception.notfound.DataNotFoundException;
import com.example.school.entity.group.Group;
import com.example.school.entity.group.GroupRepository;
import com.example.school.request.AttendanceRequest;
import com.example.school.security.jwt.JwtService;
import com.example.school.entity.student.Student;
import com.example.school.entity.student.StudentRepository;
import com.example.school.entity.subject.Subject;
import com.example.school.entity.subject.SubjectRepository;
import com.example.school.entity.teacher.TeacherRepository;
import com.example.school.entity.user.User;
import com.example.school.entity.user.UserDetailsServiceImpl;
import com.example.school.entity.user.UserData;
import com.example.school.entity.user.permission.Permission;
import com.example.school.entity.user.permission.Role;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AttendService {

    public static final Logger logger = LoggerFactory.getLogger(AttendService.class);

    private final AttendRepository attendRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final GroupRepository groupRepository;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userService;

    public List<AttendanceDTO> attendForStudentDay(AttendanceRequest request) {
        if (request.isEmpty()) {
            logger.error("Request is empty.");
            return new ArrayList<>();
        }
        final String token = request.getToken();
        final Long groupId = request.getGroupId();
        final LocalDate date = request.getDate();
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new DataNotFoundException("Group with give id not found. Termination of operation."));




        //TODO: attend

        return attendRepository.findAttendsByStudentAndDate(null, date);
    }


    public List<Group> getSelectorsData(String accessToken) {
        String username = jwtService.extractUsername(accessToken);
        User user = userService.loadUserByUsername(username);
        Set<Permission> permissions = user.getRole().getPermissions();

        if (permissions.contains(Permission.ATTENDANCE_WATCH)) {
            UserData userData = userService.getUserData(user.getId());
            Role role = user.getRole();

            if (role.is(Role.TEACHER)) {
                return groupRepository.findAllByTeacher(userData);
            } else if (role.is(Role.STUDENT)) {
                Student student = (Student) userData;
                return List.of(student.getGroup());
            } else if (role.is(Role.PARENT)) {
                return groupRepository.findAllByParent(userData);
            } else if (permissions.contains(Permission.ATTENDANCE_EDIT)) {
                return groupRepository.findAll();
            } else {
                return new ArrayList<>();
            }

        } else {
            logger.warn("User do not have access to watch attendance.");
            return new ArrayList<>(); //TODO: fix
        }
    }

    public List<Subject> subjectsByDay(LocalDate date) {
        //TODO: attend
        return null;
    }
}
