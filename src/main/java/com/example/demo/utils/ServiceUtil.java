package com.example.demo.utils;

import com.example.demo.parent.ParentRepository;
import com.example.demo.student.StudentRepository;
import com.example.demo.teacher.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceUtil {
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

    public boolean isPhoneTakenIdNot(String phone, Long id) {
        if (phone == null || id == null) {
            return false;
        }

        if (studentRepository.existsStudentByPhoneAndIdNot(phone, id)
            || teacherRepository.existsTeacherByPhoneAndIdNot(phone, id)
            || parentRepository.existsParentByPhoneAndIdNot(phone, id)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isEmailTakenIdNot(String email, Long id) {
        if (email == null || id == null) {
            return false;
        }

        if (studentRepository.existsStudentByEmailAndIdNot(email, id)
            || teacherRepository.existsTeacherByEmailAndIdNot(email, id)
            || parentRepository.existsParentByEmailAndIdNot(email, id)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPhoneTaken(String phone) {
        if (phone == null) {
            return false;
        }

        return studentRepository.existsByPhone(phone) ||
                teacherRepository.existsByPhone(phone) ||
                parentRepository.existsByPhone(phone);
    }

    public boolean isEmailTaken(String email) {
        if (email == null) {
            return false;
        }

        return studentRepository.existsByEmail(email) ||
                teacherRepository.existsByEmail(email) ||
                parentRepository.existsByEmail(email);
    }

    public boolean existsById(Long id) {
        return parentRepository.existsById(id) ||
                teacherRepository.existsById(id) ||
                studentRepository.existsById(id);
    }


}
