package com.example.school.entity.user.permission;

import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

@RequiredArgsConstructor
public enum Role {
    ADMIN(
            Set.of(
                    Permission.ADMIN,
                    Permission.ATTENDANCE_WATCH,
                    Permission.USER

            )
    ),
    DIRECTOR(
            Set.of(
                    Permission.ADMIN,
                    Permission.ATTENDANCE_WATCH,
                    Permission.USER

            )
    ),
    OFFICE(
            Set.of(
                    Permission.ATTENDANCE_WATCH,
                    Permission.USER

            )
    ),
    TEACHER(
            Set.of(
                    Permission.ATTENDANCE_WATCH,
                    Permission.USER

            )
    ),
    STAFF(
            Set.of(
                    Permission.ATTENDANCE_WATCH,
                    Permission.USER

            )
    ),
    STUDENT(
            Set.of(
                    Permission.ATTENDANCE_WATCH,
                    Permission.USER

            )
    ),
    PARENT(
            Set.of(
                    Permission.ATTENDANCE_WATCH,
                    Permission.USER
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

    public boolean is(Role... role) {
        for (Role r : role) {
            if (this.equals(r)) {
                return true;
            }
        }
        return false;
    }

}
