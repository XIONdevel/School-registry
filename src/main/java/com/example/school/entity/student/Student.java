package com.example.school.entity.student;

import com.example.school.entity.group.Group;
import com.example.school.entity.parent.Parent;
import com.example.school.entity.user.User;
import com.example.school.entity.user.UserData;
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
public class Student extends UserData {

    @Id
    @GeneratedValue
    private Long id;
    private String firstname;
    private String lastname;
    private LocalDate dob;
    @Column(unique = true)
    private String phone;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) && Objects.equals(dob, student.dob) && Objects.equals(phone, student.phone) && Objects.equals(user, student.user) && Objects.equals(group, student.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dob, phone, user, group);
    }
}
