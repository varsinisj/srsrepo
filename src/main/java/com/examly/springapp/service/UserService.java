package com.examly.springapp.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.examly.springapp.model.User;
import com.examly.springapp.repository.UserRepository;
import com.examly.springapp.userspecifications.UserSpecifications;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        user.setActive(true);
        user.setVerified(false);
        return userRepository.save(user);
    }
      public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Optional<User> authenticateUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPasswordHash())) {
            return userOpt;
        }
        return Optional.empty();
    }

    public Page<User> searchUsers(String name, String company, String skill, Pageable pageable) {
        Specification<User> spec = Specification.where(UserSpecifications.hasName(name))
                .and(UserSpecifications.hasCompany(company))
                .and(UserSpecifications.hasSkills(skill));
        return userRepository.findAll(spec, pageable);
    }

    public java.util.List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Update fields; update email and userType only if allowed by business rules
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setUserType(updatedUser.getUserType());

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setProfilePictureUrl(updatedUser.getProfilePictureUrl());

        if (updatedUser.getPasswordHash() != null && !updatedUser.getPasswordHash().isEmpty()) {
            existingUser.setPasswordHash(passwordEncoder.encode(updatedUser.getPasswordHash()));
        }

        existingUser.setVerified(updatedUser.isVerified());
        existingUser.setActive(updatedUser.isActive());

        return userRepository.save(existingUser);
    }

    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setActive(false);
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User verifyUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setVerified(true);
        return userRepository.save(user);
    }
 }
// package com.examly.springapp.service;

// import java.util.*;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.jpa.domain.Specification;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import com.examly.springapp.model.User;
// import com.examly.springapp.repository.UserRepository;
// import com.examly.springapp.userspecifications.JwtUtil;
// import com.examly.springapp.userspecifications.UserSpecifications;

// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;

// @Service
// @Transactional
// public class UserService implements UserDetailsService {

//     @Autowired
//     private UserRepository userRepository;

//     private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

//     public User registerUser(User user) {
//         if (userRepository.findByEmail(user.getEmail()).isPresent()) {
//             throw new IllegalArgumentException("Email already registered");
//         }
//         user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
//         user.setActive(true);
//         user.setVerified(false);
//         return userRepository.save(user);
//     }

//     public Optional<User> authenticateUser(String email, String password) {
//         Optional<User> userOpt = userRepository.findByEmail(email);
//         if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPasswordHash())) {
//             return userOpt;
//         }
//         return Optional.empty();
//     }

//     // Optional: JWT token generation method (requires JwtUtil injected)
//     @Autowired
//     private JwtUtil jwtUtil;

//     public Optional<String> loginAndGenerateToken(String email, String password) {
//         Optional<User> userOpt = authenticateUser(email, password);
//         return userOpt.map(user -> generateJwtToken(user));
//     }

//     public String generateJwtToken(User user) {
//         return jwtUtil.generateToken(user.getEmail(), user.getUserType().toString());
//     }

//     public Page<User> searchUsers(String name, String company, String skill, Pageable pageable) {
//         Specification<User> spec = Specification.where(UserSpecifications.hasName(name))
//                 .and(UserSpecifications.hasCompany(company))
//                 .and(UserSpecifications.hasSkills(skill));
//         return userRepository.findAll(spec, pageable);
//     }

//     public List<User> getAllUsers() {
//         return userRepository.findAll();
//     }

//     public Optional<User> getUserById(Long id) {
//         return userRepository.findById(id);
//     }

//     public User updateUser(Long id, User updatedUser) {
//         User existingUser = userRepository.findById(id)
//                 .orElseThrow(() -> new IllegalArgumentException("User not found"));

//         existingUser.setEmail(updatedUser.getEmail());
//         existingUser.setUserType(updatedUser.getUserType());

//         existingUser.setFirstName(updatedUser.getFirstName());
//         existingUser.setLastName(updatedUser.getLastName());
//         existingUser.setProfilePictureUrl(updatedUser.getProfilePictureUrl());

//         if (updatedUser.getPasswordHash() != null && !updatedUser.getPasswordHash().isEmpty()) {
//             existingUser.setPasswordHash(passwordEncoder.encode(updatedUser.getPasswordHash()));
//         }

//         existingUser.setVerified(updatedUser.isVerified());
//         existingUser.setActive(updatedUser.isActive());

//         return userRepository.save(existingUser);
//     }

//     public void deactivateUser(Long id) {
//         User user = userRepository.findById(id)
//                 .orElseThrow(() -> new IllegalArgumentException("User not found"));
//         user.setActive(false);
//         userRepository.save(user);
//     }

//     public void deleteUser(Long id) {
//         userRepository.deleteById(id);
//     }

//     public User verifyUser(Long id) {
//         User user = userRepository.findById(id)
//                 .orElseThrow(() -> new IllegalArgumentException("User not found"));
//         user.setVerified(true);
//         return userRepository.save(user);
//     }

//     // Implement loadUserByUsername for Spring Security authentication
//     @Override
//     public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//         User user = userRepository.findByEmail(email)
//             .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

//         return new org.springframework.security.core.userdetails.User(
//             user.getEmail(),
//             user.getPasswordHash(),
//             Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getUserType().toString()))
//         );
//     }
// }
