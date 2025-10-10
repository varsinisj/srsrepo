package com.examly.springapp.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_profiles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;
 
    @Column(name = "expected_graduation_year", nullable = false)
    private Integer expectedGraduationYear;

    @Column(name = "academic_interests", columnDefinition = "json")
    private String academicInterests;

    @Column(name = "career_goals", columnDefinition = "TEXT")
    private String careerGoals;

    @Column(columnDefinition = "json")
    private String skills;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Builder.Default
    @Column(name = "is_mentee", nullable = false)
    private boolean isMentee = false;
}
