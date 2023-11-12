package com.example.demo.utils;

import com.example.demo.parent.ParentRepository;
import com.example.demo.student.StudentRepository;
import com.example.demo.teacher.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceUtil {
    //add new repositories here
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    @Autowired
    public ServiceUtil(ParentRepository parentRepository,
                       StudentRepository studentRepository,
                       TeacherRepository teacherRepository) {
        this.parentRepository = parentRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    public boolean isEmailTaken(String email) {
        if (email == null) {
            return false;
        }

        if (studentRepository.existsByEmail(email) ||
                teacherRepository.existsByEmail(email) ||
                parentRepository.existsByEmail(email)) {
            return true;
        }
        return false;
    }

    public boolean isPhoneTaken(String phone) {
        if (phone == null) {
            return false;
        }

        if (studentRepository.existsByPhone(phone) ||
                teacherRepository.existsByPhone(phone) ||
                parentRepository.existsByPhone(phone)) {
            return true;
        }
        return false;
    }




}
