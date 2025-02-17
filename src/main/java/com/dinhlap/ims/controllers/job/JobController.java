package com.dinhlap.ims.controllers.job;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/jobs")
public class JobController {

    @GetMapping
    public String getAllJobs() {
        return "contents/jobs/job-list";
    }

    @PostMapping("/delete/{id}")
    public String deleteJob(@PathVariable("id") Long jobId) {
        return "redirect:/jobs";
    }

    @GetMapping("/create")
    public String createJob() {
        return "contents/jobs/job-create";
    }

    @GetMapping("/detail/{id}")
    public String detailJob(@PathVariable("id") Long id) {
        return "contents/jobs/job-detail";
    }

    @GetMapping("/edit/{id}")
    public String editJob(@PathVariable("id") Long id) {
        return "contents/jobs/job-edit";
    }
}
