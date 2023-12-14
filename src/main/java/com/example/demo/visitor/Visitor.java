package com.example.demo.visitor;

import com.example.demo.user.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class Visitor {

    @Id
    @GeneratedValue
    private Long id;
    private String firstname;
    private String lastname;
    @Column(unique = true)
    private String phone;
    private Date entry;
    private Date exit;
    @Enumerated(EnumType.STRING)
    private Role role;
}
