package com.example.demo.parents;

import com.example.demo.student.Student;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Parent {
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

    @Column(length = 50,
            unique = true)
    private String email;

    @Transient
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "student_parent",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Student> children = new HashSet<>();

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Student> getChildren() {
        return children;
    }

    public void addChildren(Student student) {
        children.add(student);
    }

    public Parent(Long id,
                  String name,
                  String surname,
                  String phone,
                  String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
    }

    public Parent(String name,
                  String surname,
                  String phone,
                  String email) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
    }

    public Parent() {
    }
}
