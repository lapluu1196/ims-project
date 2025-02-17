package com.dinhlap.ims.services;

import com.dinhlap.ims.dtos.job.JobCreateDTO;
import com.dinhlap.ims.dtos.job.JobDTO;
import com.dinhlap.ims.dtos.job.JobEditDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobService {

    Page<JobDTO> findAll(String search, String status, Pageable pageable);

    String save(JobCreateDTO jobCreateDTO);

    String update(JobEditDTO jobEditDTO);

    JobDTO findById(Long id);

    void deleteJobById(Long jobId);

    List<JobDTO> getJobByStatus(String status);

    void updateJobStatus(Long jobId, String status);

    void autoUpdateJobStatus();
}
