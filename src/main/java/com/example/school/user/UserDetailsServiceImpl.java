package com.example.school.user;

import com.example.school.exception.EmailTakenException;
import com.example.school.exception.notfound.DataNotFoundException;
import com.example.school.exception.notfound.UserNotFoundException;
import com.example.school.parent.ParentRepository;
import com.example.school.parent.ParentService;
import com.example.school.student.StudentRepository;
import com.example.school.student.StudentService;
import com.example.school.teacher.TeacherRepository;
import com.example.school.teacher.TeacherService;
import com.example.school.user.permission.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ParentRepository parentRepository;

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final ParentService parentService;

    public UserDetailsServiceImpl(UserRepository userRepository,
                                  StudentRepository studentRepository,
                                  TeacherRepository teacherRepository,
                                  ParentRepository parentRepository,
                                  StudentService studentService,
                                  TeacherService teacherService,
                                  ParentService parentService) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.parentRepository = parentRepository;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.parentService = parentService;
    }

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

    public UserInterface getUserData(Long userId) {
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

        if (role.is(Role.STUDENT)) {
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

        logger.error("User data with given id not found. Termination of operation.");
        throw new UserNotFoundException("User data with given id not found.");
    }

    public void deleteUser(Long id) {
        if (id == null) {
            logger.error("Given id is null. Termination of operation.");
            throw new NullPointerException("Given id is null.");
        }
        studentService.deleteStudent(id);
        teacherService.deleteTeacher(id);
        parentService.deleteParent(id);

        userRepository.deleteById(id);
    }

}
