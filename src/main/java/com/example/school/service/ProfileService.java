package com.example.school.service;

import com.example.school.exception.notfound.DataNotFoundException;
import com.example.school.entity.parent.Parent;
import com.example.school.security.jwt.JwtService;
import com.example.school.entity.staff.Staff;
import com.example.school.entity.student.Student;
import com.example.school.entity.teacher.Teacher;
import com.example.school.entity.user.User;
import com.example.school.entity.user.UserDetailsServiceImpl;
import com.example.school.entity.user.UserData;
import com.example.school.entity.user.permission.Role;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);
    private final UserDetailsServiceImpl userService;
    private final JwtService jwtService;


    public UserData getProfile(
            HttpServletRequest request
    ) {
        User user = extractUser(request);
        return userService.getUserData(user.getId());
    }

    public UserData getData(HttpServletRequest request) {
        User user = extractUser(request);

        try {
            UserData data = userService.getUserData(user.getId());
            data.setUser(null);
            return data;
        } catch (DataNotFoundException e) {
            Role role = user.getRole();
            if (role.is(Role.STUDENT, Role.ADMIN)) { //TODO: remove
                return new Student();
            } else if (role.is(Role.PARENT)) {
                return new Parent();
            } else if (role.is(Role.TEACHER)) {
                return new Teacher();
            } else if (role.is(Role.STAFF, Role.ADMIN, Role.DIRECTOR, Role.OFFICE)) {
                return new Staff();
            } else {
                throw new IllegalArgumentException("Given role is wrong");
            }
        }
    }

    public void saveData(HttpServletRequest request, UserData data) {
        User user = extractUser(request);
        userService.saveUserData(user, data);
    }

    protected User extractUser(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        String jwt;
        String username;

        jwt = authHeader.substring(7);
        username = jwtService.extractUsername(jwt);

        return userService.loadUserByUsername(username);
    }
}
