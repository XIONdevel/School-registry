package com.example.demo.student;

import com.example.demo.group.Group;
import com.example.demo.parent.Parent;
import com.example.demo.user.User;
import com.example.demo.user.UserInterface;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Student")
public class Student implements UserInterface {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String surname;
    @Column(unique = true)
    private String phone;
    private LocalDate dob;
    @Column(unique = true)
    private String email;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToMany (cascade = CascadeType.ALL)
    @JoinTable (
            name = "student_parent",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "parent_id")
    )
    private Set<Parent> parents = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;


    public void addParent(Parent parent) {
        parents.add(parent);
    }

    public void removeParent(Parent parent) {
        parents.remove(parent);
    }
}
