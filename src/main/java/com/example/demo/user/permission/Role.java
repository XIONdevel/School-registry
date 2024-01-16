package com.example.demo.user.permission;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {
    ADMIN(
            Set.of(
                    Permission.ADMIN
            )
    ),
    DIRECTOR(
            Set.of(
                    Permission.ADMIN
            )
    ),
    OFFICE(
            Set.of(
                    Permission.CREATE_STUDENT,
                    Permission.CREATE_TEACHER,
                    Permission.CREATE_STAFF,
                    Permission.EDIT_STUDENT,
                    Permission.EDIT_TEACHER,
                    Permission.EDIT_STAFF,
                    Permission.DELETE_STUDENT,
                    Permission.DELETE_TEACHER,
                    Permission.DELETE_STAFF
            )
    ),
    TEACHER(
            Set.of(

            )
    ),
    STAFF(
            Set.of(
                    Permission.CREATE_STUDENT,
                    Permission.CREATE_TEACHER,
                    Permission.EDIT_STUDENT,
                    Permission.EDIT_TEACHER,
                    Permission.DELETE_STUDENT,
                    Permission.DELETE_TEACHER
            )
    ),
    STUDENT(
            Set.of(

            )
    ),
    PARENT(
            Set.of(

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


}
