package com.example.school.entity.staff;

import com.example.school.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    Optional<Staff> findAllByPosition(Position position);


    Optional<Staff> findByPhone(String phone);

    Optional<Staff> findByUser(User user);

    Boolean existsStaffByPhoneAndIdNot(String phone, Long id);

    Boolean existsByPhone(String phone);

}
