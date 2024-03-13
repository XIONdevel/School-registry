package com.example.school.security.token;

import com.example.school.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);
    private final TokenRepository tokenRepository;

    public Token save(Token token) {
        if (!token.isEmpty()) {
            revoke(token.getUser());
            return tokenRepository.save(token);
        } else {
            logger.error("Error while saving token. Termination of operation.");
            throw new IllegalArgumentException("Error while saving token.");
        }
    }

    public boolean revoke(User user) {
        if (user == null || user.getId() == null) {
            return false;
        } else {
            tokenRepository.deleteAllByUser(user);
            return true;
        }
    }



}
