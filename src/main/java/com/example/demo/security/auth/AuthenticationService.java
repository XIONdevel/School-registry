package com.example.demo.security.auth;

import com.example.demo.exception.jwt.InvalidTokenException;
import com.example.demo.security.jwt.JwtService;
import com.example.demo.security.token.Token;
import com.example.demo.security.token.TokenRepository;
import com.example.demo.security.token.TokenService;
import com.example.demo.user.User;
import com.example.demo.user.UserDetailsServiceImpl;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

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

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws AuthenticationException {
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
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .cookie(cookie)
                .build();
    }

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .isEnable(true)
                .build();

        User saved = userService.createUser(user);
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user).getToken();
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) (refreshExpiration / 1000));
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .cookie(cookie)
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) {
        String refreshToken;
        String username;

        if (request.getRefreshToken() == null) {
            logger.error("Refresh token from request is null. Termination of operation.");
            throw new IllegalArgumentException("Refresh token from request is null.");
        }

        refreshToken = request.getRefreshToken();
        username = jwtService.extractUsername(refreshToken);

        if (username != null) {
            User user = userService.loadUserByUsername(username);
            if (jwtService.isRefreshTokenValid(refreshToken, user)) {
                Cookie cookie = new Cookie("refreshToken", refreshToken);
                cookie.setHttpOnly(true);
                cookie.setMaxAge((int) (refreshExpiration / 1000));
                return AuthenticationResponse.builder()
                        .accessToken(jwtService.generateToken(user))
                        .cookie(cookie)
                        .build();
            } else {
                tokenService.revoke(user);
                logger.warn("Refresh token is expired. Redirection to login page.");
                throw new InvalidTokenException("Refresh token is expired.");
            }
        } else {
            logger.error("Username is null. Termination of operation.");
            throw new NullPointerException("Username is null.");
        }
    }
}
