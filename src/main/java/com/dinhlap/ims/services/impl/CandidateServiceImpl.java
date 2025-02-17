package com.dinhlap.ims.services.impl;

import com.dinhlap.ims.dtos.candidate.CandidateDTO;
import com.dinhlap.ims.entities.Candidate;
import com.dinhlap.ims.exceptions.AppException;
import com.dinhlap.ims.exceptions.ErrorCode;
import com.dinhlap.ims.repositories.CandidateRepository;
import com.dinhlap.ims.repositories.UserRepository;
import com.dinhlap.ims.services.CandidateService;
import com.dinhlap.ims.services.LogService;
import com.dinhlap.ims.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LogService logService;

    @Autowired
    private UserService userService;

    @Override
    public Page<CandidateDTO> findAll(String search, String status, Pageable pageable) {

        Specification<Candidate> spec = (Specification<Candidate>) (root, query, criteriaBuilder) -> {
            if ((search == null || search.isEmpty()) && (status == null || status.isEmpty())) {
                return null;
            }

            if (search == null || search.isEmpty()) {
                return criteriaBuilder.equal(root.get("status"), status);
            }

            if (status == null || status.isEmpty()) {
                return criteriaBuilder.or(
                        criteriaBuilder.like(root.get("fullName"), "%" + search + "%"),
                        criteriaBuilder.like(root.get("email"), "%" + search + "%"),
                        criteriaBuilder.like(root.get("phoneNumber"), "%" + search + "%"),
                        criteriaBuilder.like(root.get("currentPosition"), "%" + search + "%"),
                        criteriaBuilder.like(root.get("status"), "%" + search + "%")
                ) ;
            }

            return criteriaBuilder.and(
                    criteriaBuilder.or(
                            criteriaBuilder.like(root.get("fullName"), "%" + search + "%"),
                            criteriaBuilder.like(root.get("email"), "%" + search + "%"),
                            criteriaBuilder.like(root.get("phoneNumber"), "%" + search + "%"),
                            criteriaBuilder.like(root.get("currentPosition"), "%" + search + "%"),
                            criteriaBuilder.like(root.get("status"), "%" + search + "%")
                    ),
                    criteriaBuilder.equal(root.get("status"), status)
            );
        };

        var candidates = candidateRepository.findAll(spec, pageable);

        return candidates.map(this::convertToDTO);
    }

    @Override
    public CandidateDTO save(CandidateDTO candidateDTO) {
        Candidate candidate = convertToEntity(candidateDTO);
        candidate.setRecruiterOwner(userRepository.findByUsername(candidateDTO.getRecruiterOwner()));

        Candidate savedCandidate = candidateRepository.save(candidate);

        logService.logAction("Create candidate", "Candidate", savedCandidate.getCandidateId(), "Candidate created");

        return convertToDTO(savedCandidate);
    }

    @Override
    public String deleteById(Long candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        if (candidate == null) {
            return "Candidate not found";
        }
        try {
            candidateRepository.deleteById(candidateId);
        } catch (DataIntegrityViolationException e) {
            return "Cannot delete candidate because of existing references to interview";
        } catch (Exception e) {
            return "Failed to delete candidate";
        }

        logService.logAction("Delete candidate", "Candidate", candidateId, "Candidate deleted");

        return "Candidate deleted successfully";
    }

    @Override
    public CandidateDTO findById(Long id) {
        Candidate candidate = candidateRepository.findById(id).orElse(null);
        if (candidate == null) {
            throw new AppException(ErrorCode.CANDIDATE_NOT_FOUND);
        }
        return convertToDTODetail(candidate);
    }

    @Override
    public String updateCandidate(CandidateDTO candidateDTO) {
        Candidate candidate = candidateRepository.findById(candidateDTO.getCandidateId()).orElse(null);
        if (candidate == null) {
            throw new RuntimeException("Candidate not found");
        }
        candidate.setFullName(candidateDTO.getFullName());
        candidate.setEmail(candidateDTO.getEmail());
        candidate.setPhoneNumber(candidateDTO.getPhoneNumber());
        candidate.setAddress(candidateDTO.getAddress());
        candidate.setDateOfBirth(candidateDTO.getDateOfBirth());
        candidate.setGender(candidateDTO.getGender());
        if (candidateDTO.getCvFileName() != null) {
            candidate.setCvFileName(candidateDTO.getCvFileName());
        }
        candidate.setCurrentPosition(candidateDTO.getCurrentPosition());
        candidate.setSkills(candidateDTO.getSkills());
        candidate.setYearsOfExperience(candidateDTO.getYearsOfExperience());
        candidate.setHighestEducationLevel(candidateDTO.getHighestEducationLevel());
        candidate.setRecruiterOwner(userRepository.findByUsername(candidateDTO.getRecruiterOwner()));
        candidate.setStatus(candidateDTO.getStatus());
        candidate.setNotes(candidateDTO.getNotes());
        candidate.setUpdatedAt(LocalDateTime.now());
        candidate = candidateRepository.save(candidate);

        logService.logAction("Update candidate", "Candidate", candidate.getCandidateId(), "Candidate updated");

        return "Candidate updated successfully";
    }

    @Override
    public boolean existsByEmail(String email) {
        return candidateRepository.existsByEmail(email);
    }

    @Override
    public String banCandidate(Long candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        if (candidate == null) {
            return "Candidate not found";
        }
        if (candidate.getStatus().equals("Banned")) {
            candidate.setStatus("Open");
        } else {
            candidate.setStatus("Banned");
        }
        candidateRepository.save(candidate);
        logService.logAction("Ban candidate", "Candidate", candidateId, "Candidate banned");

        return candidate.getStatus();
    }

    @Override
    public Candidate getCandidateById(Long candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        if (candidate == null) {
            throw new RuntimeException("Candidate not found");
        }
        return candidate;
    }

    @Override
    public List<CandidateDTO> getCandidateByStatus(String status) {
        List<Candidate> candidates = candidateRepository.getCandidateByStatus(status);

        return candidates.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public void updateCandidateStatus(Long candidateId, String status) {
        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        if (candidate == null) {
            throw new RuntimeException("Candidate not found");
        }
        candidate.setStatus(status);
        candidateRepository.save(candidate);
    }

    private CandidateDTO convertToDTO(Candidate candidate) {
        return new CandidateDTO(
                candidate.getCandidateId(),
                candidate.getFullName(),
                candidate.getEmail(),
                candidate.getPhoneNumber(),
                candidate.getAddress(),
                candidate.getDateOfBirth(),
                candidate.getGender(),
                candidate.getCvFileName(),
                candidate.getCurrentPosition(),
                candidate.getSkills(),
                candidate.getYearsOfExperience(),
                candidate.getHighestEducationLevel(),
                candidate.getRecruiterOwner().getUsername(),
                candidate.getStatus(),
                candidate.getNotes(),
                candidate.getCreatedAt().toLocalDate(),
                candidate.getUpdatedAt().toLocalDate()
        );
    }

    private CandidateDTO convertToDTODetail(Candidate candidate) {
        return new CandidateDTO(
                candidate.getCandidateId(),
                candidate.getFullName(),
                candidate.getEmail(),
                candidate.getPhoneNumber(),
                candidate.getAddress(),
                candidate.getDateOfBirth(),
                candidate.getGender(),
                candidate.getCvFileName(),
                candidate.getCurrentPosition(),
                candidate.getSkills(),
                candidate.getYearsOfExperience(),
                candidate.getHighestEducationLevel(),
                candidate.getRecruiterOwner().getUsername(),
                candidate.getStatus(),
                candidate.getNotes(),
                candidate.getCreatedAt().toLocalDate(),
                candidate.getUpdatedAt().toLocalDate()
        );
    }

    private Candidate convertToEntity(CandidateDTO candidateDTO) {
        return new Candidate(
                candidateDTO.getCandidateId(),
                candidateDTO.getFullName(),
                candidateDTO.getEmail(),
                candidateDTO.getPhoneNumber(),
                candidateDTO.getAddress(),
                candidateDTO.getDateOfBirth(),
                candidateDTO.getGender(),
                candidateDTO.getCvFileName(),
                candidateDTO.getCurrentPosition(),
                candidateDTO.getSkills(),
                candidateDTO.getYearsOfExperience(),
                candidateDTO.getHighestEducationLevel(),
                null,
                candidateDTO.getStatus(),
                candidateDTO.getNotes(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}

