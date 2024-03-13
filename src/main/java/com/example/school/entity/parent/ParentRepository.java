package com.example.school.entity.parent;

import com.example.school.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {

    Optional<Parent> findByUser(User user);

    boolean existsByPhone(String phone);

    boolean existsParentByPhoneAndIdNot(String phone, Long id);

}
