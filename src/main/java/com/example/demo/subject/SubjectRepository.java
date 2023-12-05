package com.example.demo.subject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    boolean existsSubjectByNameAndIdNot(String name, Long id);

    Optional<Subject> findByName(String name);
}
