package com.example.school.group;

import com.example.school.dto.AttendanceGroupSelectorDTO;
import com.example.school.user.UserInterface;
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
    List<Group> findAllByTeacher(@Param("teacher") UserInterface userData);

    @Query("""
    SELECT g
    FROM Parent p
    JOIN p.children ch
    JOIN ch.group g
    WHERE p =:parent
    """)
    List<Group> findAllByParent(@Param("parent") UserInterface userData);


}



