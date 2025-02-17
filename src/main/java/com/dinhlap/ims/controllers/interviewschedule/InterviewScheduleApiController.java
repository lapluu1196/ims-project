package com.dinhlap.ims.controllers.interviewschedule;

import com.dinhlap.ims.dtos.interviewschedule.ScheduleDetailDTO;
import com.dinhlap.ims.dtos.interviewschedule.ScheduleEditDTO;
import com.dinhlap.ims.dtos.interviewschedule.ScheduleListDTO;
import com.dinhlap.ims.services.InterviewScheduleService;
import com.dinhlap.ims.utils.GetSiteUrl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/interview-schedules")
public class InterviewScheduleApiController {

    @Autowired
    private InterviewScheduleService interviewScheduleService;

    @GetMapping
    public ResponseEntity<Page<ScheduleListDTO>> getAllJobs(@RequestParam(required = false) String search,
                                                            @RequestParam(required = false) Long interviewerId,
                                                            @RequestParam(required = false) String status,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        var pageable = PageRequest.of(page, size);
        try {
            return ResponseEntity.ok(interviewScheduleService.findAll(search, interviewerId, status, pageable));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDetailDTO> getSchedule(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(interviewScheduleService.getScheduleDetail(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<ScheduleEditDTO> getScheduleEdit(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(interviewScheduleService.getScheduleEdit(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateSchedule(@PathVariable("id") Long id,
                                                 @Valid @RequestBody ScheduleEditDTO scheduleEditDTO) {
        try {
            return ResponseEntity.ok(interviewScheduleService.update(scheduleEditDTO));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelSchedule(@RequestParam("scheduleId") Long id) {
        try {
            return ResponseEntity.ok(interviewScheduleService.cancelSchedule(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/send-reminder")
    public ResponseEntity<String> sendReminder(@RequestParam("id") Long id, HttpServletRequest request) {
        String interviewUrl = GetSiteUrl.getSiteUrl(request) + "/interview-schedules/detail/" + id;

        interviewScheduleService.sendReminder(id, interviewUrl);

        return ResponseEntity.ok("Email reminder has been sent to the interviewers.");
    }
}
