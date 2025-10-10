package com.examly.springapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examly.springapp.model.StudentProfile;
import com.examly.springapp.service.StudentProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/student-profiles")
@RequiredArgsConstructor
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<StudentProfile> getProfile(@PathVariable Long userId) {
        StudentProfile profile = studentProfileService.getProfile(userId);
        if (profile != null) return ResponseEntity.ok(profile);
        else return ResponseEntity.notFound().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<StudentProfile> updateProfile(@PathVariable Long userId, @RequestBody StudentProfile profile) {
        StudentProfile updated = studentProfileService.updateProfile(userId, profile);
        return ResponseEntity.ok(updated);
    }
}
