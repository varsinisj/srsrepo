package com.examly.springapp.controller;

// import com.examly.springapp.model.User;
// import com.examly.springapp.service.AdminService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.Map;

// @RestController
// @RequestMapping("/api/v1/admin")
// @RequiredArgsConstructor
// public class AdminController {

//     private final AdminService adminService;

//     @PostMapping("/log")
//     public ResponseEntity<Void> logAdminAction(@RequestParam Long adminId, @RequestParam String action) {
//         adminService.logAction(adminId, action);
//         return ResponseEntity.ok().build();
//     }

//     @GetMapping("/user-counts")
//     public ResponseEntity<Map<User.UserType, Long>> getUserCounts() {
//         return ResponseEntity.ok(adminService.getUserCounts());
//     }
// }
import com.examly.springapp.model.User;
import com.examly.springapp.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    
    // Logging endpoint (already your code)
    @PostMapping("/log")
    public ResponseEntity<Void> logAdminAction(@RequestParam Long adminId, @RequestParam String action) {
        // Call logging service here
        return ResponseEntity.ok().build();
    }

    // Get all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    // Get user by id
    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = adminService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    // Add new user
    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User newUser) {
        return ResponseEntity.ok(adminService.addUser(newUser));
    }

    // Update user
    @PutMapping("/users/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User updatedUser) {
        return ResponseEntity.ok(adminService.updateUser(userId, updatedUser));
    }

    // Deactivate user (patch)
    @PatchMapping("/users/{userId}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long userId) {
        adminService.deactivateUser(userId);
        return ResponseEntity.noContent().build();
    }

    // Delete user
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    // Get counts by user type
    @GetMapping("/user-counts")
    public ResponseEntity<Map<User.UserType, Long>> getUserCounts() {
        return ResponseEntity.ok(adminService.getUserCounts());
    }
}
