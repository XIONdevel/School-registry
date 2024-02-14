package com.example.school.security.auth;

import com.example.school.security.auth.request.AuthenticationRequest;
import com.example.school.security.auth.request.LogoutRequest;
import com.example.school.security.auth.request.RegisterRequest;
import com.example.school.user.permission.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
//    @PreAuthorize("hasAuthority('USER')")
    public void refreshToken(
            @RequestBody HttpServletRequest request,
            @RequestBody HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }

    @PostMapping("/logout")
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
    public ResponseEntity<Boolean> isLoggedIn() {
        System.out.println("accessed successfully");
        return ResponseEntity.ok(true);
    }
}
