package com.examly.springapp.service;



// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import com.examly.springapp.model.AuditLog;
// import com.examly.springapp.model.User;
// import com.examly.springapp.repository.AuditLogRepository;
// import com.examly.springapp.repository.UserRepository;

// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.Map;
// import java.util.stream.Collectors;

// @Service
// @RequiredArgsConstructor
// @Transactional
// public class AdminService {

//     private final UserRepository userRepository;
//     private final AuditLogRepository auditLogRepository;

//     public void logAction(Long adminId, String action) {
//         AuditLog auditLog = AuditLog.builder()
//                 .adminId(adminId)
//                 .action(action)
//                 .timestamp(LocalDateTime.now())
//                 .build();
//         auditLogRepository.save(auditLog);
//     }

//     public Map<User.UserType, Long> getUserCounts() {
//         List<User> allUsers = userRepository.findAll();
//         return allUsers.stream()
//                 .collect(Collectors.groupingBy(User::getUserType, Collectors.counting()));
//     }
// }
// package com.examly.springapp.service;



// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import com.examly.springapp.model.AuditLog;
// import com.examly.springapp.model.User;
// import com.examly.springapp.repository.AuditLogRepository;
// import com.examly.springapp.repository.UserRepository;

// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.Map;
// import java.util.stream.Collectors;

// @Service
// @RequiredArgsConstructor
// @Transactional
// public class AdminService {

//     private final UserRepository userRepository;
//     private final AuditLogRepository auditLogRepository;

//     public void logAction(Long adminId, String action) {
//         AuditLog auditLog = AuditLog.builder()
//                 .adminId(adminId)
//                 .action(action)
//                 .timestamp(LocalDateTime.now())
//                 .build();
//         auditLogRepository.save(auditLog);
//     }

//     public Map<User.UserType, Long> getUserCounts() {
//         List<User> allUsers = userRepository.findAll();
//         return allUsers.stream()
//                 .collect(Collectors.groupingBy(User::getUserType, Collectors.counting()));
//     }
// }
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.UserRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor


@Transactional
public class AdminService {

    private final UserRepository userRepository;

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User addUser(User newUser) {
        // Optionally check here for admin permissions
        return userRepository.save(newUser);
    }

    public User updateUser(Long id, User updatedUser) {
    User existing = getUserById(id);
    existing.setEmail(updatedUser.getEmail());
    existing.setFirstName(updatedUser.getFirstName());
    existing.setLastName(updatedUser.getLastName());
    existing.setUserType(updatedUser.getUserType());
    existing.setProfilePictureUrl(updatedUser.getProfilePictureUrl());
    existing.setVerified(updatedUser.isVerified());
    existing.setActive(updatedUser.isActive());
    return userRepository.save(existing);
}

public void deactivateUser(Long id) {
    User user = getUserById(id);
    user.setActive(false);
    userRepository.save(user);
}


   

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Optional: Get counts grouped by type
    public Map<User.UserType, Long> getUserCounts() {
        return userRepository.findAll()
            .stream()
            .collect(Collectors.groupingBy(User::getUserType, Collectors.counting()));
    }
}
