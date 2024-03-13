package com.example.school.entity.user;

import com.example.school.entity.user.permission.Role;
import com.example.school.exception.EmailTakenException;
import com.example.school.exception.notfound.DataNotFoundException;
import com.example.school.exception.notfound.UserNotFoundException;
import com.example.school.entity.parent.Parent;
import com.example.school.entity.parent.ParentRepository;
import com.example.school.entity.parent.ParentService;
import com.example.school.entity.staff.Staff;
import com.example.school.entity.staff.StaffRepository;
import com.example.school.entity.staff.StaffService;
import com.example.school.entity.student.Student;
import com.example.school.entity.student.StudentRepository;
import com.example.school.entity.student.StudentService;
import com.example.school.entity.teacher.Teacher;
import com.example.school.entity.teacher.TeacherRepository;
import com.example.school.entity.teacher.TeacherService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ParentRepository parentRepository;
    private final StaffRepository staffRepository;

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final ParentService parentService;
    private final StaffService staffService;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null) {
            logger.error("Given username is null. Termination of operation.");
            throw new NullPointerException("Given username is null.");
        }

        Optional<User> optionalUser = userRepository.findByEmail(username);

        if (optionalUser.isEmpty()) {
            logger.error("User with given email not found. Termination of operation.");
            throw new UserNotFoundException("User with given email not found.");
        }

        return optionalUser.get();
    }

    public User createUser(User user) {
        if (user == null) {
            logger.error("Given user is null. Termination of operation.");
            throw new NullPointerException("Given user is null.");
        }

        String email = user.getEmail();
        Role role = user.getRole();

        if (userRepository.existsByEmail(email)) {
            logger.error("Email is taken. Termination of operation.");
            throw new EmailTakenException("Email is taken.");
        }

        User savedUser = userRepository.save(user);
        logger.info("User successfully saved.");
        return savedUser;
    }

    public UserData getUserData(Long userId) {
        if (userId == null) {
            logger.error("Given user id is null. Termination of operation.");
            throw new NullPointerException("Given user id is null.");
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            logger.error("User with given id not found. Termination of operation.");
            throw new UserNotFoundException("User with given id not found.");
        }
        User user = optionalUser.get();
        Role role = user.getRole();

        if (role.is(Role.STUDENT, Role.ADMIN)) {
            return studentRepository.findByUser(user)
                    .orElseThrow(() -> new DataNotFoundException("Student data for this user not found."));
        }
        if (role.is(Role.PARENT)) {
            return parentRepository.findByUser(user)
                    .orElseThrow(() -> new DataNotFoundException("Parent data for this user not found."));
        }
        if (role.is(Role.TEACHER)) {
            return teacherRepository.findByUser(user)
                    .orElseThrow(() -> new DataNotFoundException("Teacher data for this user not found."));
        }
        if (role.is(Role.STAFF, Role.ADMIN, Role.DIRECTOR, Role.OFFICE)) {
            return staffRepository.findByUser(user)
                    .orElseThrow(() -> new DataNotFoundException("Staff data for this user not found."));
        }

        logger.error("User data with given id not found. Termination of operation.");
        throw new UserNotFoundException("User data with given id not found.");
    }

    //TODO: optimize this sh1t
    public void saveUserData(User user, UserData data) {
        final Role role = user.getRole();
        if (role == null) {
            logger.error("Role of given user is null. Termination of operation");
            throw new NullPointerException("Role of the current user is null");
        } else if (role.is(Role.STUDENT)) {
            Student student = (Student) data;
            if (user.getId().equals(student.getId())) {
                studentService.saveStudent(student);
            }
        } else if (role.is(Role.PARENT)) {
            Parent parent = (Parent) data;
            if (user.getId().equals(parent.getId())) {
                parentService.saveParent(parent);
            }
        } else if (role.is(Role.TEACHER)) {
            Teacher teacher = (Teacher) data;
            if (user.getId().equals(teacher.getId())) {
                teacherService.saveTeacher(teacher);
            }
        } else if (role.is(Role.STAFF, Role.ADMIN, Role.DIRECTOR, Role.OFFICE)) {
            Staff staff = (Staff) data;
            if (user.getId().equals(staff.getId())) {
                staffService.saveStaff(staff);
            }
        }
    }

    public void deleteUser(Long id) {
        if (id == null) {
            logger.error("Given id is null. Termination of operation.");
            throw new NullPointerException("Given id is null.");
        }
        studentService.deleteStudent(id);
        teacherService.deleteTeacher(id);
        parentService.deleteParent(id);
        staffService.deleteStaff(id);

        userRepository.deleteById(id);
    }

}
