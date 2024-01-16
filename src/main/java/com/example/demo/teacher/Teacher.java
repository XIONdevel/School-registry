package com.example.demo.teacher;

import com.example.demo.group.Group;
import com.example.demo.subject.Subject;
import com.example.demo.user.User;
import com.example.demo.user.UserInterface;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Teacher")
public class Teacher implements UserInterface {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String surname;
    @Column(unique = true)
    private String phone;
    private LocalDate dob;
    private String position;
    private Integer salary;
    @Column(unique = true)
    private String email;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "teacher_subject",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> subjects = new HashSet<>();

    @OneToOne(mappedBy = "teacherLead")
    private Group mainGroup;

    public void addSubject(Subject subject) {
        subjects.add(subject);
    }

    public void removeSubject(Subject subject) {
        subjects.remove(subject);
    }

    public void removeGroup() {
        this.mainGroup = null;
    }
}
