package com.dinhlap.ims.repositories;

import com.dinhlap.ims.entities.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

    List<Job> getJobByStatus(String status);

    List<Job> findAllByStatusAndStartDate(String status, LocalDate startDate);
}
