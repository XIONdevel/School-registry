package com.example.demo.parent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {

    boolean existsByPhoneOrEmail(String phone, String email);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsParentByEmailAndIdNot(String email, Long id);

    boolean existsParentByPhoneAndIdNot(String phone, Long id);

}
