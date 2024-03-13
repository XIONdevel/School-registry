package com.example.school.security.auth;

import com.example.school.security.auth.request.AuthenticationRequest;
import com.example.school.security.auth.request.RegisterRequest;
import com.example.school.entity.user.permission.Role;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @RequestBody RegisterRequest request,
            HttpServletResponse response
    ) {
        service.register(request, response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Void> authenticate(
            @RequestBody AuthenticationRequest request,
            HttpServletResponse response
    ) {
        service.authenticate(request, response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        System.out.println("Cookie cus: " + Arrays.toString(request.getCookies()));
        for (Cookie c : request.getCookies()) {
            String text = c.getName() + ": " + c.getValue();
            System.out.println(text);
        }
        service.refreshToken(request, response);
    }

    @GetMapping("/logout")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<String> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        service.logout(request, response);
        return ResponseEntity.ok("Successfully logged out");
    }

    @GetMapping("/getRoles")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Role[]> getRoles() {
        return ResponseEntity.ok(Role.values());
    }

    @PostMapping("/loginCheck")
    public ResponseEntity<Void> isLoggedIn() {
        System.out.println("accessed successfully");
        return ResponseEntity.ok().build();
    }
}
