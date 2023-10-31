package com.example.demo.student;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

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

    public void setPhone(String phone_number) {
        this.phone = phone_number;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Student() {
    }

    public Student(String name,
                   String surname,
                   String phone,
                   LocalDate dob) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.dob = dob;
    }

    public Student(Long id,
                   String name,
                   String surname,
                   String phone,
                   LocalDate dob) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.dob = dob;
    }
}
