package com.dinhlap.ims.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Offers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "offer_id")
    private Long offerId;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "approver", nullable = false)
    private User approver;

    @Column(name = "contract_type", nullable = false, length = 50)
    private String contractType;

    @Column(name = "level", nullable = false, length = 255)
    private String level;

    @Column(name = "position", length = 255)
    private String position;

    @Column(name = "department", nullable = false, length = 255)
    private String department;

    @Column(name = "interview_info", length = 255, columnDefinition = "NVARCHAR(255)")
    private String interviewInfo;

    @Column(name = "recruiter_owner", nullable = false, length = 255, columnDefinition = "NVARCHAR(255)")
    private String recruiterOwner;

    @Column(name = "contract_period_from", nullable = false)
    private LocalDate contractPeriodFrom;

    @Column(name = "contract_period_to", nullable = false)
    private LocalDate contractPeriodTo;

    @Column(name = "basic_salary", nullable = false)
    private Double basicSalary;

    @Column(name = "offer_status", nullable = false, length = 50)
    private String offerStatus;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "notes", length = 1000)
    private String notes;

    // Minh
    private String modifiedBy;

    private String interviewNote;
    // kết thúc

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}
