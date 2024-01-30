package com.example.school.user.permission;

import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

@RequiredArgsConstructor
public enum Role {
    ADMIN(
            Set.of(
                    Permission.ADMIN,
                    Permission.ATTENDANCE_WATCH

            )
    ),
    DIRECTOR(
            Set.of(
                    Permission.ADMIN,
                    Permission.ATTENDANCE_WATCH

            )
    ),
    OFFICE(
            Set.of(
                    Permission.ATTENDANCE_WATCH

            )
    ),
    TEACHER(
            Set.of(
                    Permission.ATTENDANCE_WATCH

            )
    ),
    STAFF(
            Set.of(
                    Permission.ATTENDANCE_WATCH

            )
    ),
    STUDENT(
            Set.of(
                    Permission.ATTENDANCE_WATCH

            )
    ),
    PARENT(
            Set.of(
                    Permission.ATTENDANCE_WATCH
            )
    );

    @Getter
    private Set<Permission> permissions;

    private Role(Set<Permission> set) {
        this.permissions = set;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new LinkedList<>();
        getPermissions().forEach(auth -> authorities.add(new SimpleGrantedAuthority(auth.name())));
        return authorities;
    }

    public boolean is(Role role) {
        return this.equals(role);
    }

}
