package com.example.school.security.auth;

import com.example.school.exception.jwt.InvalidTokenException;
import com.example.school.exception.notfound.DataNotFoundException;
import com.example.school.security.auth.request.AuthenticationRequest;
import com.example.school.security.auth.request.RegisterRequest;
import com.example.school.security.jwt.JwtService;
import com.example.school.security.token.Token;
import com.example.school.security.token.TokenRepository;
import com.example.school.security.token.TokenService;
import com.example.school.entity.user.User;
import com.example.school.entity.user.UserDetailsServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    @Value("${application.security.jwt.refresh.expiration}")
    private long refreshExpiration;

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final TokenService tokenService;

    public void authenticate(AuthenticationRequest request, HttpServletResponse response) throws AuthenticationException {
        User user = userService.loadUserByUsername(request.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user).getToken();
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) (refreshExpiration / 1000));

        response.addCookie(cookie);
        response.addHeader("Authorization", accessToken);
    }

    public void register(RegisterRequest request, HttpServletResponse response) {
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .isEnable(true)
                .build();

        userService.createUser(user);
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user).getToken();
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) (refreshExpiration / 1000));

        response.addCookie(cookie);
        response.addHeader("Authorization", accessToken);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        logger.info("Started refresh token process");
        Token refresh = extractRefreshToken(request);
        String refreshToken = refresh.getToken();
        String username;

        username = jwtService.extractUsername(refreshToken);

        if (username != null) {
            User user = userService.loadUserByUsername(username);
            if (jwtService.isRefreshTokenValid(refreshToken, user)) {
                Cookie cookie = new Cookie("refreshToken", refreshToken);
                cookie.setHttpOnly(true);
                cookie.setMaxAge((int) (refreshExpiration / 1000));
                String accessToken = jwtService.generateToken(user);
                response.addHeader("Authorization", accessToken);
                response.addCookie(cookie);
            } else {
                tokenService.revoke(user);
                logger.warn("Refresh token is expired. Redirection to login page.");
                response.addCookie(new Cookie("refreshToken", "empty"));
                response.setStatus(401);
            }
        } else {
            logger.error("Username is null. Termination of operation.");
            try {
                response.sendError(417, "Username is null");
            } catch (IOException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Token refresh = extractRefreshToken(request);
        User user = refresh.getUser();
        if (jwtService.isRefreshTokenValid(refresh.getToken(), user)) {
            tokenService.revoke(user);
            Cookie cookie = new Cookie("refreshToken", "");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            try {
                response.sendRedirect("/api/v1/main/");
            } catch (IOException ignore) {}
        } else {
            logger.warn("Refresh token is invalid.");
            throw new InvalidTokenException("Refresh token is invalid.");
        }
    }

    protected Token extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies) {
            if (c.getName().equals("refreshToken")) {
                return tokenRepository.findByToken(c.getValue());
            }
        }
        logger.warn("Missing refresh token cookie in request");
        throw new DataNotFoundException("Missing refresh token cookie in request");
    }

}
