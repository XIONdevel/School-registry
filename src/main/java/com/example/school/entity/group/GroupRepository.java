package com.example.school.entity.group;

import com.example.school.entity.user.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    boolean existsByName(String name);

    boolean existsGroupByNameAndIdNot(String name, Long id);

    Optional<Group> findByName(String name);

    @Query("""
    SELECT g
    FROM Group g
    WHERE g.teacherLead =:teacher
    """)
    List<Group> findAllByTeacher(@Param("teacher") UserData userData);

    @Query("""
    SELECT g
    FROM Parent p
    JOIN p.children ch
    JOIN ch.group g
    WHERE p =:parent
    """)
    List<Group> findAllByParent(@Param("parent") UserData userData);


}



