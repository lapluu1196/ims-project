package com.dinhlap.ims.controllers.interviewschedule;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/interview-schedules")
public class InterviewScheduleController {

    @GetMapping
    public String interviewScheduleList() {
        return "contents/interviewSchedule/schedule-list";
    }

    @GetMapping("/create")
    public String interviewScheduleCreate() {
        return "contents/interviewSchedule/schedule-create";
    }

    @GetMapping("/detail/{id}")
    public String detailInterviewSchedule(@PathVariable("id") Long id) {
        return "contents/interviewSchedule/schedule-detail";
    }

    @GetMapping("/edit/{id}")
    public String editInterviewSchedule(@PathVariable("id") Long id) {
        return "contents/interviewSchedule/schedule-edit";
    }

    @GetMapping("/submit-result/{id}")
    public String submitResult(@PathVariable("id") Long id) {
        return "contents/interviewSchedule/schedule-submit-result";
    }
}
