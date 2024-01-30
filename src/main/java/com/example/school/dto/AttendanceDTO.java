package com.example.school.dto;

import com.example.school.attendence.VisitStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceDTO {

    private VisitStatus status;
    private LocalDate date;
    private String cause;

    private String studentName;
    private String studentSurname;

    private String subjectName;

}
