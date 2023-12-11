package com.example.demo.student;

import com.example.demo.group.Group;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StudentRepositoryTest {

    @Autowired
    private StudentRepository repository;

    @AfterAll
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void itShouldCheckIfStudentExistsByPhone() {
        //given
        String phone = "+123123213";
        Student testStudent = new Student();
        testStudent.setPhone(phone);

        repository.save(testStudent);

        //when
        boolean exists = repository.existsByPhone(phone);

        //then
        assertThat(exists).isTrue();
    }

    @Test
    void itShouldCheckIfStudentDoNotExistsByPhone() {
        //given
        String phone = "+123123213";

        //when
        boolean exists = repository.existsByPhone(phone);

        //then
        assertThat(exists).isFalse();
    }

    @Test
    void itShouldFindAllStudentByGroupId() {
        //given
        Group group = new Group(1L, "groupName");
        Student studentOne = new Student();
        studentOne.setName("name1");
        studentOne.setGroup(group);

        Student studentTwo = new Student();
        studentTwo.setName("name2");
        studentTwo.setGroup(group);

        repository.saveAll(List.of(studentOne, studentTwo));

        //when
        List<Student> students = repository.findAllByGroup(group);

        //then
        assertThat(students.size()).isEqualTo(2);
        assertThat(studentOne).isEqualTo(students.get(0));
        assertThat(studentTwo).isEqualTo(students.get(1));
    }

    @Test
    void itShouldCheckIfEmailTakenWithAnotherId() {
        //given
        Student studentTest = new Student();
        studentTest.setPhone("+2803929331");
        repository.save(studentTest);

        //when
        boolean notExists = repository
                .existsStudentByPhoneAndIdNot(studentTest.getPhone(), 1L);
        boolean exists = repository
                .existsStudentByPhoneAndIdNot(studentTest.getPhone(), 2L);

        //then
        assertThat(notExists).isFalse();
        assertThat(exists).isTrue();
    }





}