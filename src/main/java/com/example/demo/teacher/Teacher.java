package com.example.demo.teacher;

import jakarta.persistence.*;

import java.time.LocalDate;

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
        @Column(length = 50)
        private String phone_number;
        private LocalDate dob;
        @Column(length = 50)
        private String position;
        @Column(length = 50)
        private String email;

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

        public String getPhone_number() {
                return phone_number;
        }

        public void setPhone_number(String phone_number) {
                this.phone_number = phone_number;
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

        public Teacher(Long id,
                       String name,
                       String surname,
                       String phone_number,
                       LocalDate dob,
                       String position,
                       String email) {
                this.id = id;
                this.name = name;
                this.surname = surname;
                this.phone_number = phone_number;
                this.dob = dob;
                this.position = position;
                this.email = email;
        }

        public Teacher(String name,
                       String surname,
                       String phone_number,
                       LocalDate dob,
                       String position,
                       String email) {
                this.name = name;
                this.surname = surname;
                this.phone_number = phone_number;
                this.dob = dob;
                this.position = position;
                this.email = email;
        }

        public Teacher() {
        }
}
