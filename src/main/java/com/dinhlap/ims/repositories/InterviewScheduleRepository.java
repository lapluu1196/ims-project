package com.dinhlap.ims.repositories;

import com.dinhlap.ims.entities.InterviewSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InterviewScheduleRepository
        extends JpaRepository<InterviewSchedule, Long>, JpaSpecificationExecutor<InterviewSchedule> {

    @Query("SELECT i FROM InterviewSchedule i " +
            "LEFT JOIN i.candidate c " +
            "LEFT JOIN i.interviewers intv " +
            "WHERE (:search IS NULL OR LOWER(i.interviewTitle) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(c.fullName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(intv.fullName) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:status IS NULL OR LOWER(i.status) LIKE LOWER(CONCAT('%', :status, '%'))) " +
            "AND (:interviewerId IS NULL OR intv.userId = :interviewerId)")
    Page<InterviewSchedule> searchAll(@Param("search") String search,
                                      @Param("interviewerId") Long interviewerId,
                                      @Param("status") String status,
                                      Pageable pageable);

    List<InterviewSchedule> findAllByStatusInAndScheduleDate(List<String> statuses, LocalDate scheduleDate);
}
