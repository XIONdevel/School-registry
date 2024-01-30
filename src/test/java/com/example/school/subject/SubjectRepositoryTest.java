package com.example.school.subject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class SubjectRepositoryTest {
    @Autowired
    private SubjectRepository subjectRepository;

    @Test
    void ifExistsSubjectByNameAndByIdNot() {
        //given
        Subject subject = new Subject("Math");
        subject.setId(1L);

        //when
        boolean exists = subjectRepository.existsSubjectByNameAndIdNot("Math", 1L);

        //then
        assertThat(exists).isFalse();
    }
}