package com.example.school.utils;

import com.example.school.entity.parent.ParentRepository;
import com.example.school.entity.staff.StaffRepository;
import com.example.school.entity.student.StudentRepository;
import com.example.school.entity.teacher.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceUtil {
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final StaffRepository staffRepository;


//    public boolean isPhoneTakenIdNot(String phone, Long id) {
//        if (phone == null || id == null) {
//            return false;
//        }
//
//        return studentRepository.existsStudentByPhoneAndIdNot(phone, id)
//                || teacherRepository.existsTeacherByPhoneAndIdNot(phone, id)
//                || parentRepository.existsParentByPhoneAndIdNot(phone, id)
//                || staffRepository.existsStaffByPhoneAndIdNot(phone, id);
//    }
//
//    public boolean isEmailTakenIdNot(String email, Long id) {
//        if (email == null || id == null) {
//            return false;
//        }
//
//        return studentRepository.existsStudentByEmailAndIdNot(email, id)
//                || teacherRepository.existsTeacherByEmailAndIdNot(email, id)
//                || parentRepository.existsParentByEmailAndIdNot(email, id)
//                || staffRepository.existsStaffByEmailAndIdNot(email, id);
//    }
//
//    public boolean isPhoneTaken(String phone) {
//        if (phone == null) {
//            return false;
//        }
//
//        return studentRepository.existsByPhone(phone) ||
//                teacherRepository.existsByPhone(phone) ||
//                parentRepository.existsByPhone(phone) ||
//                staffRepository.existsByPhone(phone);
//    }
//
//    public boolean isEmailTaken(String email) {
//        if (email == null) {
//            return false;
//        }
//
//        return studentRepository.existsByEmail(email) ||
//                teacherRepository.existsByEmail(email) ||
//                parentRepository.existsByEmail(email) ||
//                staffRepository.existsByEmail(email);
//    }
//
//    public boolean existsById(Long id) {
//        return parentRepository.existsById(id) ||
//                teacherRepository.existsById(id) ||
//                studentRepository.existsById(id) ||
//                staffRepository.existsById(id);
//    }


}
