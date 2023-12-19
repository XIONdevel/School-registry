package com.example.demo.user;

import com.example.demo.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null) {
            logger.error("Given username is null. Termination of operation.");
            throw new NullPointerException("Given username is null.");
        }

        Optional<User> optionalUser = userRepository.findByEmail(username);

        if (optionalUser.isEmpty()) {
            logger.error("User with given username not found. Termination of operation.");
            throw new UserNotFoundException("User with given username not found.");
        }

        return optionalUser.get();
    }

}
