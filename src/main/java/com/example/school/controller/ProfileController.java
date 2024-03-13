package com.example.school.controller;

import com.example.school.service.ProfileService;
import com.example.school.entity.user.UserData;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/get")
    public ResponseEntity<Object> getProfileData(
            HttpServletRequest request
    ) {
       return ResponseEntity.ok(profileService.getProfile(request));
    }

    @PostMapping("/edit")
    public ResponseEntity<Object> getData(
            HttpServletRequest request
    ) {
        System.out.println("Endpoint post edit");
        return ResponseEntity.ok(profileService.getData(request));
    }

    @PutMapping("/edit")
    public ResponseEntity<Void> saveData(
            HttpServletRequest request,
            @RequestBody UserData data
            ) {
        profileService.saveData(request, data);
        return ResponseEntity.ok().build();
    }




}
