package com.example.demo.subject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Boolean existsSubjectByName(String name);

}
