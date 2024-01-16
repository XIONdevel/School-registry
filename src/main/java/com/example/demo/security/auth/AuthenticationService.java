package com.example.demo.security.auth;

import com.example.demo.security.jwt.JwtService;
import com.example.demo.user.User;
import com.example.demo.user.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws AuthenticationException {
        logger.debug("auth email: {}", request.getEmail());
        logger.debug("auth password: {}", request.getPassword());
        User user = userService.loadUserByUsername(request.getEmail());
        logger.debug("User: {}, user id: {}", user, user.getId());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        String token = jwtService.generateToken(user);
        logger.debug("Generated token: {}, for user id: {}", token, user.getId());
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .isEnable(true)
                .build();

        User saved = userService.createUser(user);
        String token = jwtService.generateToken(user);
        logger.info(String.format("Token: %s, generated for id: %d", token, saved.getId()));
        return new AuthenticationResponse(token);
    }


}
