package com.dinhlap.ims.services;

import com.dinhlap.ims.dtos.candidate.CandidateDTO;
import com.dinhlap.ims.entities.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CandidateService {

     Page<CandidateDTO> findAll(String search, String status, Pageable pageable);

     String deleteById(Long candidateId);

     CandidateDTO findById(Long id);

     CandidateDTO save(CandidateDTO candidateDTO);

     String updateCandidate(CandidateDTO candidateDTO);

     boolean existsByEmail(String email);

     String banCandidate(Long candidateId);

     Candidate getCandidateById(Long candidateId);

     List<CandidateDTO> getCandidateByStatus(String status);

     void updateCandidateStatus(Long candidateId, String status);

}
