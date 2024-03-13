package com.example.school.security.token;

import com.example.school.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {


    void deleteAllByUser(User user);

    Token findByToken(String token);

    boolean existsByToken(String token);
}
