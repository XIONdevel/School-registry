package com.example.school.entity.staff;

import com.example.school.entity.user.User;
import com.example.school.entity.user.UserData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Staff extends UserData {

    @Id
    private Long id;
    private String firstname;
    private String lastname;
    @Column(unique = true)
    private String phone;
    private LocalDate dob;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Enumerated(EnumType.STRING)
    private Position position;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Staff staff = (Staff) o;
        return Objects.equals(id, staff.id) && Objects.equals(phone, staff.phone) && Objects.equals(dob, staff.dob) && position == staff.position;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, phone, dob, position);
    }
}
