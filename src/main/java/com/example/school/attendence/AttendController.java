package com.example.school.attendence;

import com.example.school.dto.AttendanceDTO;
import com.example.school.entity.group.Group;
import com.example.school.request.AttendanceRequest;
import com.example.school.entity.subject.Subject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attend")
public class AttendController {

    private final AttendService attendService;

    @PostMapping("/get/attendance")
    public ResponseEntity<List<AttendanceDTO>> getAttendance(
            @RequestBody AttendanceRequest request
    ) {
        return ResponseEntity.ok(attendService.attendForStudentDay(request));
    }

    @PostMapping("/get/subjects")
    public ResponseEntity<List<Subject>> getSubjects(LocalDate date) {
        return ResponseEntity.ok(attendService.subjectsByDay(date));
    }

    @PostMapping("/selectors")
    public ResponseEntity<List<Group>> getSelectors(
            @RequestBody String accessToken
    ) {
        return ResponseEntity.ok(attendService.getSelectorsData(accessToken));
    }


}
