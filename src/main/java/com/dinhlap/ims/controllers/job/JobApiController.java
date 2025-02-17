package com.dinhlap.ims.controllers.job;

import com.dinhlap.ims.dtos.job.JobCreateDTO;
import com.dinhlap.ims.dtos.job.JobDTO;
import com.dinhlap.ims.dtos.job.JobEditDTO;
import com.dinhlap.ims.services.JobService;
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
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobApiController {
    private final JobService jobService;

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

    @GetMapping("/{id}")
    public ResponseEntity<JobDTO> getJob(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(jobService.findById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateJob(@Valid @RequestBody JobEditDTO jobEditDTO) {
        try {
            return ResponseEntity.ok(jobService.update(jobEditDTO));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

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

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody JobCreateDTO jobCreateDTO) {
        try {
            return ResponseEntity.ok(jobService.save(jobCreateDTO));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

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
