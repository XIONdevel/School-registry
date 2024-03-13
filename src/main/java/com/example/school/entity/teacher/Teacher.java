package com.example.school.entity.teacher;

import com.example.school.entity.group.Group;
import com.example.school.entity.subject.Subject;
import com.example.school.entity.user.User;
import com.example.school.entity.user.UserData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Teacher")
public class Teacher extends UserData {

    @Id
    @GeneratedValue
    private Long id;
    private String firstname;
    private String lastname;
    @Column(unique = true)
    private String phone;
    private LocalDate dob;
    private String position;

    @OneToOne(mappedBy = "teacherLead")
    private Group mainGroup;

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



    public void addSubject(Subject subject) {
        subjects.add(subject);
    }

    public void removeSubject(Subject subject) {
        subjects.remove(subject);
    }

    public void removeGroup() {
        this.mainGroup = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(id, teacher.id) && Objects.equals(phone, teacher.phone) && Objects.equals(dob, teacher.dob) && Objects.equals(position, teacher.position) && Objects.equals(mainGroup, teacher.mainGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, phone, dob, position, mainGroup);
    }
}
