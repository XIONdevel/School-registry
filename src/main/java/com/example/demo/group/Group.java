package com.example.demo.group;

import com.example.demo.teacher.Teacher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "group")
public class Group {
    @Id
    @GeneratedValue
    private Long id;
    @Column (unique = true)
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teacherlead_id")
    private Teacher teacherLead;

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

    public Teacher getTeacherLead() {
        return teacherLead;
    }

    public void addTeacherLead(Teacher teacherLead) {
        this.teacherLead = teacherLead;
    }

    public Group(String name) {
        this.name = name;
    }

    public Group(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public boolean isTaken() {
        return this.teacherLead != null;
    }

    public void removeTeacher() {
        this.teacherLead = null;
    }
}
