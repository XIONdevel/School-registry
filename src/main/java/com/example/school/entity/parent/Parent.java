package com.example.school.entity.parent;

import com.example.school.entity.student.Student;
import com.example.school.entity.user.User;
import com.example.school.entity.user.UserData;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "parent")
public class Parent extends UserData {

    @Id
    @GeneratedValue
    private Long id;
    private String firstname;
    private String lastname;
    @Column(unique = true)
    private String phone;
    private LocalDate dob;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(mappedBy = "parents")
    private Set<Student> children = new HashSet<>();

    public void addChild(Student student) {
        children.add(student);
    }

    public void removeChild(Student student) {
        this.children.remove(student);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parent parent = (Parent) o;
        return Objects.equals(id, parent.id) && Objects.equals(phone, parent.phone) && Objects.equals(dob, parent.dob);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, phone, dob);
    }
}
