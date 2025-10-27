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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    
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

    // Activate user (patch)
    @PatchMapping("/users/{userId}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable Long userId) {
        adminService.activateUser(userId);
        return ResponseEntity.noContent().build();
    }

    // Delete user
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long userId) {
        try {
            adminService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        } catch (org.springframework.dao.DataIntegrityViolationException dive) {
            // likely referenced by other entities (constraints) - return 409 Conflict with message
            log.warn("Failed to delete user {} due to integrity constraints", userId, dive);
            Map<String, String> body = new HashMap<>();
            body.put("error", "conflict");
            body.put("message", "User cannot be deleted because it is referenced by other records.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        } catch (IllegalArgumentException iae) {
            log.info("User {} not found for deletion", userId, iae);
            Map<String, String> body = new HashMap<>();
            body.put("error", "not_found");
            body.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        } catch (Exception e) {
            log.error("Unexpected error deleting user {}", userId, e);
            Map<String, String> body = new HashMap<>();
            body.put("error", "internal_error");
            body.put("message", "An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }

    // Get counts by user type
    @GetMapping("/user-counts")
    public ResponseEntity<Map<User.UserType, Long>> getUserCounts() {
        return ResponseEntity.ok(adminService.getUserCounts());
    }
}
