package com.dinhlap.ims.dtos.interviewschedule;

import com.dinhlap.ims.validates.EndTimeAfterStartTime;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EndTimeAfterStartTime(startTimeField = "scheduleFrom", endTimeField = "scheduleTo")
public class ScheduleEditDTO {

    private Long scheduleId;

    @NotBlank(message = "Schedule title is required!")
    private String scheduleTitle;

    @NotNull(message = "Candidate name is required!")
    private Long candidateId;

    @NotNull(message = "Job is required!")
    private Long jobId;

    @NotBlank(message = "Interviewer is required!")
    private String interviewersIdList;

    @NotNull(message = "Schedule time is required!")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate scheduleDate;

    @NotNull(message = "Schedule time is required!")
    private LocalTime scheduleFrom;

    @NotNull(message = "Schedule time is required!")
    private LocalTime scheduleTo;

    private String location;

    @NotNull(message = "Recruiter owner is required!")
    private Long recruiterOwner;

    private String meetingId;

    private String notes;

    private String result;

    private String status;

}
