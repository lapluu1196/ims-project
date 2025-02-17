package com.dinhlap.ims.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "InterviewSchedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @Column(name = "interview_title", nullable = false, length = 255, columnDefinition = "NVARCHAR(255)")
    private String interviewTitle;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(name = "schedule_date")
    private LocalDate scheduleDate;

    @Column(name = "schedule_from")
    private LocalTime scheduleFrom;

    @Column(name = "schedule_to")
    private LocalTime scheduleTo;

    @Column(name = "location", length = 255, columnDefinition = "NVARCHAR(255)")
    private String location;

    @ManyToMany
    @JoinTable(
            name = "interview_schedule_interviewers",
            joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> interviewers;

    @Column(name = "recruiter_owner", nullable = false)
    private Long recruiterOwner;

    @Column(name = "meeting_id", length = 255)
    private String meetingId;

    @Column(name = "notes", length = 1000, columnDefinition = "NVARCHAR(1000)")
    private String notes;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "result", length = 50)
    private String result;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt = LocalDateTime.now();
}
