package com.dinhlap.ims.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "password" , nullable = false, length = 255)
    private String password;

    @Column(name = "full_name", nullable = false, length = 150, columnDefinition = "NVARCHAR(150)")
    private String fullName;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "address", length = 255, columnDefinition = "NVARCHAR(255)")
    private String address;

    @Column(name = "gender", nullable = false, length = 50)
    private String gender;

    @Column(name = "department", nullable = false, length = 100)
    private String department;

    @Column(name = "role", nullable = false, length = 50)
    private String role;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "Notes", length = 1000, columnDefinition = "NVARCHAR(1000)")
    private String notes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Quan hệ nhiều-nhiều với InterviewSchedule
    @ManyToMany(mappedBy = "interviewers")
    private List<InterviewSchedule> interviewSchedules;

    // Quan hệ 1-n với PasswordResetToken
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<PasswordResetToken> passwordResetTokens;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username +
                ", password='" + password +
                ", fullName='" + fullName +
                ", email='" + email +
                ", phoneNumber='" + phoneNumber +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address +
                ", gender='" + gender +
                ", department='" + department +
                ", role='" + role +
                ", status='" + status +
                ", notes='" + notes +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
