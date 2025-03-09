package com.dinhlap.ims.controllers.interviewschedule;

import com.dinhlap.ims.dtos.interviewschedule.ScheduleDetailDTO;
import com.dinhlap.ims.dtos.interviewschedule.ScheduleEditDTO;
import com.dinhlap.ims.dtos.interviewschedule.ScheduleListDTO;
import com.dinhlap.ims.services.InterviewScheduleService;
import com.dinhlap.ims.utils.GetSiteUrl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Interview Schedules", description = "APIs for managing interview schedules")
@RequestMapping("/api/interview-schedules")
public class InterviewScheduleApiController {

    @Autowired
    private InterviewScheduleService interviewScheduleService;

    @Operation(summary = "Get all interview schedules", description = "Retrieve a paginated list of interview schedules with optional search and filter parameters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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

    @Operation(summary = "Get interview schedule details", description = "Retrieve details of a specific interview schedule by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved details",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ScheduleDetailDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDetailDTO> getSchedule(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(interviewScheduleService.getScheduleDetail(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @Operation(summary = "Get editable interview schedule", description = "Retrieve an editable version of a specific interview schedule by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved editable schedule",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ScheduleEditDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/edit/{id}")
    public ResponseEntity<ScheduleEditDTO> getScheduleEdit(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(interviewScheduleService.getScheduleEdit(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @Operation(summary = "Update interview schedule", description = "Update an existing interview schedule. Requires Admin, Manager, or Recruiter role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated schedule"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAnyAuthority('Admin', 'Manager', 'Recruiter')")
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

    @Operation(summary = "Cancel an interview schedule", description = "Cancel an existing interview schedule. Requires Admin or Recruiter role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully canceled schedule"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAnyAuthority('Admin', 'Recruiter')")
    @PostMapping("/cancel")
    public ResponseEntity<String> cancelSchedule(@RequestParam("scheduleId") Long id) {
        try {
            return ResponseEntity.ok(interviewScheduleService.cancelSchedule(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @Operation(summary = "Send interview reminder", description = "Send a reminder email to interviewers for a specific interview schedule. Requires Admin or Interviewer role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email reminder sent successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAnyAuthority('Admin', 'Interviewer')")
    @PostMapping("/send-reminder")
    public ResponseEntity<String> sendReminder(@RequestParam("id") Long id, HttpServletRequest request) {
        String interviewUrl = GetSiteUrl.getSiteUrl(request) + "/interview-schedules/detail/" + id;

        interviewScheduleService.sendReminder(id, interviewUrl);

        return ResponseEntity.ok("Email reminder has been sent to the interviewers.");
    }
}
