package com.example.school.group;

import com.example.school.entity.group.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class GroupRepositoryTest {
    @Autowired
    private GroupRepository groupRepository;

}