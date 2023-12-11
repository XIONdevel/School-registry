package com.example.demo.teacher;

import com.example.demo.group.Group;
import com.example.demo.subject.Subject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Teacher")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    @Column(unique = true)
    private String phone;
    private LocalDate dob;
    private String position;
    @Column(unique = true)
    private String email;

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

    public Teacher(Long id,
                   String name,
                   String surname,
                   String phone,
                   LocalDate dob,
                   String position,
                   String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.dob = dob;
        this.position = position;
        this.email = email;
    }

    public Group getGroup() {
        return mainGroup;
    }

    public void addGroup(Group mainGroup) {
        this.mainGroup = mainGroup;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public Teacher(String name,
                   String surname,
                   String phone,
                   LocalDate dob,
                   String position,
                   String email) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.dob = dob;
        this.position = position;
        this.email = email;
    }

    public void removeGroup() {
        this.mainGroup = null;
    }
}
