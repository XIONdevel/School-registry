package com.example.demo.parent;

import com.example.demo.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {

    Optional<Parent> findByUser(User user);

    boolean existsByPhoneOrEmail(String phone, String email);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsParentByEmailAndIdNot(String email, Long id);

    boolean existsParentByPhoneAndIdNot(String phone, Long id);

}
