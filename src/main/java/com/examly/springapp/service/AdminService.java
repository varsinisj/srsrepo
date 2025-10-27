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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.examly.springapp.model.User;
import com.examly.springapp.model.Message;
import com.examly.springapp.model.Connection;
import com.examly.springapp.model.AlumniProfile;
import com.examly.springapp.model.StudentProfile;
import com.examly.springapp.model.PasswordResetToken;
import com.examly.springapp.repository.UserRepository;
import com.examly.springapp.repository.MessageRepository;
import com.examly.springapp.repository.ConnectionRepository;
import com.examly.springapp.repository.AlumniProfileRepository;
import com.examly.springapp.repository.StudentProfileRepository;
import com.examly.springapp.repository.PasswordResetTokenRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor


@Transactional
public class AdminService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ConnectionRepository connectionRepository;
    private final AlumniProfileRepository alumniProfileRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private static final Logger log = LoggerFactory.getLogger(AdminService.class);

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

public void activateUser(Long id) {
    User user = getUserById(id);
    user.setActive(true);
    userRepository.save(user);
}


   

    public void deleteUser(Long id) {
        // Remove messages sent or received by user
        List<Message> sent = messageRepository.findBySenderId(id);
        if (!sent.isEmpty()) {
            messageRepository.deleteAll(sent);
            log.info("Deleted {} sent messages for user {}", sent.size(), id);
        }
        List<Message> received = messageRepository.findByReceiverId(id);
        if (!received.isEmpty()) {
            messageRepository.deleteAll(received);
            log.info("Deleted {} received messages for user {}", received.size(), id);
        }

        // Remove connections where user is sender or receiver
        List<Connection> sentConns = connectionRepository.findBySenderId(id);
        if (!sentConns.isEmpty()) {
            connectionRepository.deleteAll(sentConns);
            log.info("Deleted {} sent connections for user {}", sentConns.size(), id);
        }
        List<Connection> recvConns = connectionRepository.findByReceiverId(id);
        if (!recvConns.isEmpty()) {
            connectionRepository.deleteAll(recvConns);
            log.info("Deleted {} received connections for user {}", recvConns.size(), id);
        }

        // Remove alumni/student profiles
        AlumniProfile ap = alumniProfileRepository.findByUserId(id);
        if (ap != null) {
            alumniProfileRepository.delete(ap);
            log.info("Deleted alumni profile for user {}", id);
        }
        StudentProfile sp = studentProfileRepository.findByUserId(id);
        if (sp != null) {
            studentProfileRepository.delete(sp);
            log.info("Deleted student profile for user {}", id);
        }

        // Remove password reset tokens
        List<PasswordResetToken> tokens = passwordResetTokenRepository.findByUserId(id);
        if (!tokens.isEmpty()) {
            passwordResetTokenRepository.deleteAll(tokens);
            log.info("Deleted {} password reset tokens for user {}", tokens.size(), id);
        }

        // Finally delete user
        userRepository.deleteById(id);
    }

    // Optional: Get counts grouped by type
    public Map<User.UserType, Long> getUserCounts() {
        return userRepository.findAll()
            .stream()
            .collect(Collectors.groupingBy(User::getUserType, Collectors.counting()));
    }
}
