package com.example.demo.student;

import com.example.demo.group.Group;
import com.example.demo.parent.Parent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Student")
public class Student implements Serializable {
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

    @Column(length = 50)
    private String email;

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


    protected void setParents(Set<Parent> parents) {
        this.parents = parents;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Column
    private LocalDate dob;

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

    public Set<Parent> getParents() {
        return parents;
    }

    public void addParents(Parent parent) {
        parents.add(parent);
    }

    public void removeParent(Parent parent) {
        parents.remove(parent);
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Student(Long id,
                   String name,
                   String surname,
                   String phone,
                   String email,
                   LocalDate dob) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.dob = dob;
    }

    public Student(String name,
                   String surname,
                   String phone,
                   String email,
                   LocalDate dob) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.dob = dob;
    }

    public Student() {
    }
}
