package com.dinhlap.ims.dtos.interviewschedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDetailDTO {

    private Long scheduleId;

    private String scheduleTitle;

    private String candidateName;

    private String jobTitle;

    private String interviewersUsernameList;

    private LocalDate scheduleDate;

    private LocalTime scheduleFrom;

    private LocalTime scheduleTo;

    private String location;

    private String recruiterOwner;

    private String meetingId;

    private String notes;

    private String result;

    private String status;

    private LocalDate createdAt;

    private LocalDate updatedAt;
}
