package com.example.demo.parent;

import com.example.demo.student.Student;
import com.example.demo.user.User;
import com.example.demo.user.UserInterface;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "parent")
public class Parent implements UserInterface {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String surname;
    @Column(unique = true)
    private String phone;
    @Column(unique = true)
    private String email;

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
}
