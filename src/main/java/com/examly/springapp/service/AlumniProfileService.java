// package com.examly.springapp.service;

// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import com.examly.springapp.model.AlumniProfile;
// import com.examly.springapp.model.User;
// import com.examly.springapp.repository.AlumniProfileRepository;
// import com.examly.springapp.repository.UserRepository;

// import lombok.RequiredArgsConstructor;

// @Service
// @RequiredArgsConstructor
// @Transactional
// public class AlumniProfileService {

//     private final AlumniProfileRepository alumniProfileRepository;
//     private final UserRepository userRepository;

//     public AlumniProfile updateProfile(Long userId, AlumniProfile profileDetails) {
//         User user = userRepository.findById(userId)
//                     .orElseThrow(() -> new IllegalArgumentException("User not found"));
//         AlumniProfile profile = alumniProfileRepository.findByUserId(userId);

//         if (profile == null) {
//             profile = new AlumniProfile();
//             profile.setUser(user);
//         }

//         profile.setGraduationYear(profileDetails.getGraduationYear());
//         profile.setIndustry(profileDetails.getIndustry());
//         profile.setLocation(profileDetails.getLocation());  // Set newly added location
//         profile.setWorkHistory(profileDetails.getWorkHistory());
//         profile.setSkills(profileDetails.getSkills());
//         profile.setBio(profileDetails.getBio());
//         profile.setMentor(profileDetails.isMentor());

//         return alumniProfileRepository.save(profile);
//     }

//     public AlumniProfile getProfile(Long userId) {
//         return alumniProfileRepository.findByUserId(userId);
//     }
// }
package com.examly.springapp.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.examly.springapp.model.AlumniProfile;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.AlumniProfileRepository;
import com.examly.springapp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AlumniProfileService {

    private final AlumniProfileRepository alumniProfileRepository;
    private final UserRepository userRepository;

    public AlumniProfile updateProfile(Long userId, AlumniProfile profileDetails) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        AlumniProfile profile = alumniProfileRepository.findByUserId(userId);

        if (profile == null) {
            profile = new AlumniProfile();
            profile.setUser(user);
        }

        profile.setGraduationYear(profileDetails.getGraduationYear());
        profile.setIndustry(profileDetails.getIndustry());
        profile.setLocation(profileDetails.getLocation());
        profile.setWorkHistory(profileDetails.getWorkHistory());
        profile.setSkills(profileDetails.getSkills());
        profile.setBio(profileDetails.getBio());
        profile.setMentor(profileDetails.isMentor());

        return alumniProfileRepository.save(profile);
    }

    public AlumniProfile getProfile(Long userId) {
        return alumniProfileRepository.findByUserId(userId);
    }

    public List<AlumniProfile> searchProfiles(String location, String graduationYear) {
        boolean hasLocation = location != null && !location.isBlank();
        boolean hasGrad = graduationYear != null && !graduationYear.isBlank();

        if (hasLocation && hasGrad) {
            Integer gradYearVal = Integer.valueOf(graduationYear);
            return alumniProfileRepository.findByLocationIgnoreCaseAndGraduationYear(location, gradYearVal);
        } else if (hasLocation) {
            return alumniProfileRepository.findByLocationIgnoreCase(location);
        } else if (hasGrad) {
            Integer gradYearVal = Integer.valueOf(graduationYear);
            return alumniProfileRepository.findByGraduationYear(gradYearVal);
        } else {
            return alumniProfileRepository.findAll();
        }
    }
}
