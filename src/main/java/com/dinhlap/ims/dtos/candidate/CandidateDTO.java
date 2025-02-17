package com.dinhlap.ims.dtos.candidate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidateDTO {
    private Long candidateId;

    @NotBlank(message = "Full name is required!")
    private String fullName;

    @NotBlank(message = "Full name is required!")
    private String email;

    private String phoneNumber;

    private String address;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Gender is required!")
    private String gender;

    private String cvFileName;

    @NotBlank(message = "Current position is required!")
    private String currentPosition;

    @NotBlank(message = "Skills is required!")
    private String skills;

    private Integer yearsOfExperience;

    @NotBlank(message = "Highest education level is required!")
    private String highestEducationLevel;

    @NotBlank(message = "Recruiter owner is required!")
    private String recruiterOwner;

    @NotBlank(message = "Status is required!")
    private String status;

    private String notes;

    private LocalDate createdAt;

    private LocalDate updatedAt;
}
