package com.example.school.entity.group;

import com.example.school.entity.teacher.Teacher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_group")
public class Group {
    @Id
    @GeneratedValue
    private Long id;
    @Column (unique = true)
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lead_id")
    private Teacher lead;

    public Group(String name) {
        this.name = name;
    }

    public boolean isTaken() {
        return this.lead != null;
    }

    public void removeTeacher() {
        this.lead = null;
    }
}
