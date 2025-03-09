package com.dinhlap.ims.controllers.job;

import com.dinhlap.ims.dtos.job.JobCreateDTO;
import com.dinhlap.ims.dtos.job.JobDTO;
import com.dinhlap.ims.dtos.job.JobEditDTO;
import com.dinhlap.ims.services.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Jobs", description = "APIs for managing jobs")
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobApiController {
    private final JobService jobService;

    @Operation(summary = "Get all jobs", description = "Retrieve a paginated list of jobs with optional search and filter parameters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<JobDTO>> getAllJobs(@RequestParam(required = false) String search,
                                                   @RequestParam(required = false) String status,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        var pageable = PageRequest.of(page, size);
        try {
            return ResponseEntity.ok(jobService.findAll(search, status, pageable));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @Operation(summary = "Get job details", description = "Retrieve details of a specific job by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved job details",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JobDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<JobDTO> getJob(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(jobService.findById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @Operation(summary = "Update job", description = "Update an existing job posting.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated job"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> updateJob(@Valid @RequestBody JobEditDTO jobEditDTO) {
        try {
            return ResponseEntity.ok(jobService.update(jobEditDTO));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @Operation(summary = "Delete job", description = "Delete a job by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted job"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        try {
            jobService.deleteJobById(id);
            return ResponseEntity.ok("Delete successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @Operation(summary = "Create job", description = "Create a new job posting.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created job"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody JobCreateDTO jobCreateDTO) {
        try {
            return ResponseEntity.ok(jobService.save(jobCreateDTO));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @Operation(summary = "Get open jobs", description = "Retrieve a list of all jobs with status 'Open'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved open jobs",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getOpenJobs")
    public ResponseEntity<List<JobDTO>> getOpenJobs() {
        try {
            return ResponseEntity.ok(jobService.getJobByStatus("Open"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

}
