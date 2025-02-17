package com.dinhlap.ims.dtos.interviewschedule;

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
public class ScheduleListDTO {

    private Long scheduleId;

    private String interviewTitle;

    private String candidateName;

    private LocalDate scheduleDate;

    @DateTimeFormat(pattern = "HH:MM")
    private LocalTime scheduleFrom;

    @DateTimeFormat(pattern = "HH:MM")
    private LocalTime scheduleTo;

    private String interviewerNames;

    private String jobTitle;

    private String status;

    private String result;

}
