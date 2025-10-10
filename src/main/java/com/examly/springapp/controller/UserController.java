// package com.examly.springapp.controller;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import com.examly.springapp.model.User;
// import com.examly.springapp.service.UserService;

// import lombok.RequiredArgsConstructor;
// import com.examly.springapp.security.JwtUtil;

// @RestController
// @RequestMapping("/api/users")
// @RequiredArgsConstructor
// public class UserController {
    
//     private final UserService userService;
//     private final JwtUtil jwtUtil;

//     @PostMapping("/register")
//     public ResponseEntity<User> register(@RequestBody User user) {
//         User created = userService.registerUser(user);
//         return ResponseEntity.ok(created);
//     }

//     @PostMapping("/login")
//     public ResponseEntity<User> login(@RequestParam String email, @RequestParam String password) {
//         return userService.authenticateUser(email, password)
//             .map(user -> {
//                 String token = jwtUtil.generateToken(user.getEmail());
//                 return ResponseEntity.ok().header("Authorization", "Bearer " + token).body(user);
//             })
//             .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<User> getUser(@PathVariable Long id) {
//         return userService.getUserById(id)
//             .map(ResponseEntity::ok)
//             .orElse(ResponseEntity.notFound().build());
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
//         User updated = userService.updateUser(id, user);
//         return ResponseEntity.ok(updated);
//     }
    
//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
//         userService.deleteUser(id);
//         return ResponseEntity.noContent().build();
//     }

//     @PutMapping("/{id}/deactivate")
//     public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
//         userService.deactivateUser(id);
//         return ResponseEntity.noContent().build();
//     }
// }
// ...existing code...
package com.examly.springapp.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.examly.springapp.model.User;
import com.examly.springapp.service.UserService;

import lombok.RequiredArgsConstructor;
import com.examly.springapp.security.JwtUtil;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User created = userService.registerUser(user);
        // hide password hash from client
        created.setPasswordHash(null);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam String email, @RequestParam String password) {
        return userService.authenticateUser(email, password)
            .map(user -> {
                String token = jwtUtil.generateToken(user.getEmail());
                user.setPasswordHash(null); // do not return hash
                return ResponseEntity.ok().header("Authorization", "Bearer " + token).body(user);
            })
            .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userService.getUserById(id)
            .map(user -> {
                user.setPasswordHash(null);
                return ResponseEntity.ok(user);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updated = userService.updateUser(id, user);
        updated.setPasswordHash(null);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<User>> listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(name = "sort", required = false) String[] sort // multiple allowed
    ) {
        Pageable pageable;
        if (sort == null || sort.length == 0) {
            pageable = PageRequest.of(page, size);
        } else {
            // build Sort from param(s). Each sort entry: property,dir  e.g. "firstName,asc"
            Sort sortObj = Sort.unsorted();
            for (String s : sort) {
                String[] parts = s.split(",");
                String prop = parts[0].trim();
                Sort.Direction dir = (parts.length > 1 && parts[1].equalsIgnoreCase("desc")) ? Sort.Direction.DESC : Sort.Direction.ASC;
                sortObj = sortObj.and(Sort.by(dir, prop));
            }
            pageable = PageRequest.of(page, size, sortObj);
        }
        Page<User> result = userService.findAll(pageable);
        // remove passwordHash from returned users
        result.getContent().forEach(u -> u.setPasswordHash(null));
        return ResponseEntity.ok(result);
    }
}
// ...existing code...