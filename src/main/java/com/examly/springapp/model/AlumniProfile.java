package com.examly.springapp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "alumni_profiles")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlumniProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(name = "graduation_year", nullable = false)
    private Integer graduationYear;

    @Column
    private String industry;

    @Column(name = "location")
    private String location;  // Newly added field

    @Column(name = "work_history", columnDefinition = "json")
    private String workHistory;

    @Column(columnDefinition = "json")
    private String skills;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Builder.Default
    @Column(name = "is_mentor", nullable = false)
    private boolean isMentor = false;
}
