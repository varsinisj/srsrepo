package com.examly.springapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.examly.springapp.model.StudentProfile;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.StudentProfileRepository;
import com.examly.springapp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentProfileService {



    private final StudentProfileRepository studentProfileRepository;
    private final UserRepository userRepository;

    public StudentProfile updateProfile(Long userId, StudentProfile profileDetails) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        StudentProfile profile = studentProfileRepository.findByUserId(userId);

        if(profile == null) {
            profile = new StudentProfile();
            profile.setUser(user);
        }

        profile.setExpectedGraduationYear(profileDetails.getExpectedGraduationYear());
        profile.setAcademicInterests(profileDetails.getAcademicInterests());
        profile.setCareerGoals(profileDetails.getCareerGoals());
        profile.setSkills(profileDetails.getSkills());
        profile.setBio(profileDetails.getBio());
        profile.setMentee(profileDetails.isMentee());

        return studentProfileRepository.save(profile);
    }

    public StudentProfile getProfile(Long userId) {
        return studentProfileRepository.findByUserId(userId);
    }
}
