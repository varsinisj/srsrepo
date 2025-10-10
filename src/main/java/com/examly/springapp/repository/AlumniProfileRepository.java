
package com.examly.springapp.repository;

import com.examly.springapp.model.AlumniProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlumniProfileRepository extends JpaRepository<AlumniProfile, Long> {
 AlumniProfile findByUserId(Long userId);
    List<AlumniProfile> findByLocationIgnoreCase(String location);

    List<AlumniProfile> findByGraduationYear(Integer graduationYear);

    List<AlumniProfile> findByLocationIgnoreCaseAndGraduationYear(String location, Integer graduationYear);
}
