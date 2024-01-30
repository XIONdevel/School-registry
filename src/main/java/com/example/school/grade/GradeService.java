package com.example.school.grade;

import com.example.school.exception.notfound.DataNotFoundException;
import com.example.school.student.Student;
import com.example.school.student.StudentRepository;
import com.example.school.subject.Subject;
import com.example.school.subject.SubjectRepository;
import com.example.school.teacher.Teacher;
import com.example.school.teacher.TeacherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class GradeService {

    private final String launchDate = "01/01/2023";
    public static final Logger logger = LoggerFactory.getLogger(GradeService.class);
    private GradeRepository gradeRepository;
    private StudentRepository studentRepository;
    private SubjectRepository subjectRepository;
    private TeacherRepository teacherRepository;

    @Autowired
    public GradeService(GradeRepository gradeRepository,
                        StudentRepository studentRepository,
                        SubjectRepository subjectRepository,
                        TeacherRepository teacherRepository) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
    }

    public GradeService() {
    }


    public List<Grade>  getForStudentByDate(Long studentId, LocalDate date) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);

        if (optionalStudent.isEmpty()) {
            logger.warn("Student with given id not found. Termination of operation.");
            throw new DataNotFoundException("Student with given id not found");
        }

        if (!isDateValid(date)) {
            logger.warn("Given date is not correct. Termination of operation");
            throw new NullPointerException("Given date is not valid");
        }

        return gradeRepository.findAllByStudentAndDate(optionalStudent.get(), date);
    }

    public List<Grade> getForStudentByDate(
            Long studentId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);

        if (optionalStudent.isEmpty()) {
            logger.warn("Student with given id not found. Termination of operation.");
            throw new DataNotFoundException("Student with given id not found");
        }

        if (!isDateValid(startDate) || !isDateValid(endDate)) {
            logger.warn("Given date is not correct. Termination of operation");
            throw new DateTimeException("Given date is not valid");
        }

        return gradeRepository.findAllByStudentAndDateRange(
                optionalStudent.get(),
                startDate,
                endDate
        );
    }

    public List<Grade> getAllByDate(LocalDate date) {
        if (isDateValid(date)) {
            return gradeRepository.findAllByDate(date);
        } else {
            logger.warn("Given date is not correct. Termination of operation");
            throw new DateTimeException("Given date is not valid");
        }
    }

    public List<Grade> getAllByDateRange(LocalDate startDate, LocalDate endDate) {
        if (isDateValid(startDate) && isDateValid(endDate)) {
            return gradeRepository.findAllByDateRange(startDate, endDate);
        } else {
            logger.warn("Given date is not correct. Termination of operation");
            throw new DateTimeException("Given date is not valid");
        }
    }

    public List<Grade> getAllForStudent(Long studentId) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);

        if (optionalStudent.isEmpty()) {
            logger.warn("Student with given id not found. Termination of operation.");
            throw new DataNotFoundException("Student with given id not found");
        }

        return gradeRepository.findAllByStudent(optionalStudent.get());
    }

    public List<Grade> getAllForStudentBySubject(Long studentId, Long subjectId) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);

        if (optionalStudent.isEmpty()) {
            logger.warn("Student with given id not found. Termination of operation.");
            throw new DataNotFoundException("Student with given id not found");
        }

        if (optionalSubject.isEmpty()) {
            logger.warn("Subject with given id not found. Termination of operation.");
            throw new DataNotFoundException("Subject with given id not found");
        }

        return gradeRepository
                .findAllByStudentAndSubject(optionalStudent.get(), optionalSubject.get());
    }

    public List<Grade> getAllForStudentBySubjectAndDate(
            Long studentId,
            Long subjectId,
            LocalDate date
    ) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);

        if (optionalStudent.isEmpty()) {
            logger.warn("Student with given id not found. Termination of operation.");
            throw new DataNotFoundException("Student with given id not found");
        }

        if (optionalSubject.isEmpty()) {
            logger.warn("Subject with given id not found. Termination of operation.");
            throw new DataNotFoundException("Subject with given id not found");
        }

        if (isDateValid(date)) {
            return gradeRepository.findAllByStudentAndSubjectAndDate(
                    optionalStudent.get(),
                    optionalSubject.get(),
                    date
            );
        } else {
            logger.warn("Given date is not correct. Termination of operation");
            throw new DateTimeException("Given date is not valid");
        }
    }

    public List<Grade> getAllForStudentBySubjectAndDate(
            Long studentId,
            Long subjectId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);

        if (optionalStudent.isEmpty()) {
            logger.warn("Student with given id not found. Termination of operation.");
            throw new DataNotFoundException("Student with given id not found");
        }

        if (optionalSubject.isEmpty()) {
            logger.warn("Subject with given id not found. Termination of operation.");
            throw new DataNotFoundException("Subject with given id not found");
        }

        if (isDateValid(startDate) && isDateValid(endDate)) {
            return gradeRepository.findAllByStudentAndSubjectAndDateRange(
                    optionalStudent.get(),
                    optionalSubject.get(),
                    startDate,
                    endDate
            );
        } else {
            logger.warn("Given date is not correct. Termination of operation");
            throw new DateTimeException("Given date is not valid");
        }
    }

    public List<Grade> getAllBySubject(Long subjectId) {
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);

        if (optionalSubject.isEmpty()) {
            logger.warn("Subject with given id not found. Termination of operation.");
            throw new DataNotFoundException("Subject with given id not found");
        }

        return gradeRepository.findAllBySubject(optionalSubject.get());
    }

    public List<Grade> getAllByTeacher(Long teacherId) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);

        if (optionalTeacher.isEmpty()) {
            logger.warn("Teacher with given id not found. Termination of operation.");
            throw new DataNotFoundException("Teacher with given id not found.");
        }
        return gradeRepository.findAllByTeacher(optionalTeacher.get());
    }

    public void addNewGrade(Grade grade) {
        if (isGradeValid(grade)) {
            grade.setId(null);
            gradeRepository.save(grade);
            logger.info("Grade was saved successfully");
        } else {
            logger.warn("Given grade is not valid. Termination of operation. {}", grade.toString());
            throw new IllegalArgumentException("Given grade is not valid");
        }
    }

    public void deleteGrade(Long gradeId) {
        gradeRepository.deleteById(gradeId);
        logger.info("Grade was deleted successfully");
    }

    protected boolean isGradeValid(Grade grade) {
        return (isDateValid(grade.getDate()) &&
                        grade.getStudent() != null &&
                        grade.getTeacher() != null &&
                        grade.getSubject() != null);
    }

    protected boolean isDateValid(LocalDate date) {
        if (
                date == null ||
                        date.isBefore(LocalDate.of(2023, 1, 1)) ||
                        date.isAfter(LocalDate.now())
        ) {
            logger.warn("Given date is not correct. Termination of operation");
            return false;
        } else {
            return true;
        }
    }
}















