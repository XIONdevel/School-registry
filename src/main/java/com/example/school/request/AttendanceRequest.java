package com.example.school.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AttendanceRequest {

    private String token;
    private Long groupId;
    private LocalDate date;

    public boolean isEmpty() {
        return token == null ||
                groupId == null ||
                date == null;
    }
}
