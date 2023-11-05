package com.example.demo.teacher;

import com.example.demo.subject.Subject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Teacher")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String surname;

    @Column(length = 50,
            unique = true)
    private String phone;

    private LocalDate dob;

    @Column(length = 50)
    private String position;

    @Column(length = 50,
            unique = true)
    private String email;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "teacher_subject",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> subjects = new HashSet<>();

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

    public Teacher() {
    }
}
