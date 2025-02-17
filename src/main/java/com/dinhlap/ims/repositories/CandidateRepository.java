package com.dinhlap.ims.repositories;

import com.dinhlap.ims.entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long>, JpaSpecificationExecutor<Candidate> {
    boolean existsByEmail(String email);

    List<Candidate> getCandidateByStatus(String status);

}
