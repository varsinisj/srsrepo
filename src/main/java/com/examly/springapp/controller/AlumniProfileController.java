// package com.examly.springapp.controller;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import com.examly.springapp.model.AlumniProfile;
// import com.examly.springapp.service.AlumniProfileService;

// import lombok.RequiredArgsConstructor;

// @RestController
// @RequestMapping("/api/alumni-profiles")
// @RequiredArgsConstructor
// public class AlumniProfileController {

//     private final AlumniProfileService alumniProfileService;

//     @GetMapping("/{userId}")
//     public ResponseEntity<AlumniProfile> getProfile(@PathVariable Long userId) {
//         AlumniProfile profile = alumniProfileService.getProfile(userId);
//         if (profile != null)
//             return ResponseEntity.ok(profile);
//         else
//             return ResponseEntity.notFound().build();
//     }

//     @PutMapping("/{userId}")
//     public ResponseEntity<AlumniProfile> updateProfile(@PathVariable Long userId,
//                                                      @RequestBody AlumniProfile profile) {
//         AlumniProfile updatedProfile = alumniProfileService.updateProfile(userId, profile);
//         return ResponseEntity.ok(updatedProfile);
//     }
// }
package com.examly.springapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.examly.springapp.model.AlumniProfile;
import com.examly.springapp.service.AlumniProfileService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/alumni-profiles")
@RequiredArgsConstructor
public class AlumniProfileController {

    private final AlumniProfileService alumniProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<AlumniProfile> getProfile(@PathVariable Long userId) {
        AlumniProfile profile = alumniProfileService.getProfile(userId);
        if (profile != null)
            return ResponseEntity.ok(profile);
        else
            return ResponseEntity.notFound().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<AlumniProfile> updateProfile(@PathVariable Long userId,
                                                       @RequestBody AlumniProfile profile) {
        AlumniProfile updatedProfile = alumniProfileService.updateProfile(userId, profile);
        return ResponseEntity.ok(updatedProfile);
    }

    // --- NEW: SEARCH ENDPOINT ---
    @GetMapping("/search")
    public ResponseEntity<List<AlumniProfile>> searchAlumniProfiles(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String graduationYear
    ) {
        List<AlumniProfile> results = alumniProfileService.searchProfiles(location, graduationYear);
        return ResponseEntity.ok(results);
    }
}
