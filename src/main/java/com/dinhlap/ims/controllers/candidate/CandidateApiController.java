package com.dinhlap.ims.controllers.candidate;

import com.dinhlap.ims.dtos.candidate.CandidateDTO;
import com.dinhlap.ims.services.CandidateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Candidate", description = "APIs for managing candidates")
@RequestMapping("/api/candidates")
public class CandidateApiController {
    @Autowired
    private CandidateService candidateService;

    @Operation(
            summary = "Get list of candidates",
            description = "Retrieve a paginated list of candidates based on search and status filters.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved candidates"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @GetMapping
    public ResponseEntity<Page<CandidateDTO>> getCandidates(@RequestParam(required = false) String search,
                                                            @RequestParam(required = false) String status,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        var pageable = PageRequest.of(page, size);
        try {
            return ResponseEntity.ok(candidateService.findAll(search, status, pageable));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @Operation(
            summary = "Create a new candidate",
            description = "Create a new candidate and upload their CV file.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Candidate created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createCandidate(@Valid @RequestPart CandidateDTO candidateDTO,
                                                  @RequestPart("cv") MultipartFile cvFile) {
        if (cvFile.isEmpty()) {
            return ResponseEntity.badRequest().body("CV file is empty");
        }
        try {
            String uploadDir = "D:\\HN24_FRF_FJW_02\\MockProject\\Output\\";

            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = UUID.randomUUID() + "_" + cvFile.getOriginalFilename();
            String filePath = uploadDir + fileName;
            File destFile = new File(filePath);
            cvFile.transferTo(destFile);

            candidateDTO.setCvFileName(fileName);

            CandidateDTO savedCandidate = candidateService.save(candidateDTO);
            if (savedCandidate != null) {
                return ResponseEntity.ok("Candidate created");
            }

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists. Please use a different email address.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload the CV file.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }

        return ResponseEntity.badRequest().body("Candidate not created");
    }

    @Operation(
            summary = "Get candidate by ID",
            description = "Retrieve details of a specific candidate.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved candidate"),
                    @ApiResponse(responseCode = "404", description = "Candidate not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CandidateDTO> getCandidate(@PathVariable("id") Long id) {

        return ResponseEntity.ok(candidateService.findById(id));
    }

    @Operation(
            summary = "Update candidate",
            description = "Update an existing candidate's information.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Candidate updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Candidate not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCandidate(@PathVariable("id") Long id,
                                                  @Valid @RequestBody CandidateDTO candidateDTO) {
        try {
            return ResponseEntity.ok(candidateService.updateCandidate(candidateDTO));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @Operation(
            summary = "Delete candidate",
            description = "Remove a candidate from the system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Candidate deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Candidate not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCandidate(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(candidateService.deleteById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @Operation(
            summary = "Ban a candidate",
            description = "Mark a candidate as banned.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Candidate banned successfully"),
                    @ApiResponse(responseCode = "404", description = "Candidate not found")
            }
    )
    @PutMapping("/banCandidate")
    public ResponseEntity<String> banCandidate(@RequestParam("candidateId") Long candidateId) {
        String result = candidateService.banCandidate(candidateId);
        if (result.equals("Candidate not found")) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @Operation(
            summary = "Download CV file",
            description = "Download a candidate's CV file.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "File downloaded successfully"),
                    @ApiResponse(responseCode = "404", description = "File not found")
            }
    )
    @GetMapping("/downloadCv")
    public ResponseEntity<Resource> downloadCv(@RequestParam("candidateId") Long candidateId) {
        CandidateDTO candidateDTO = candidateService.findById(candidateId);

        if (candidateDTO.getCvFileName() == null || candidateDTO.getCvFileName().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String uploadDir = "D:\\HN24_FRF_FJW_02\\MockProject\\Output\\";

        Path filePath = Paths.get(uploadDir + candidateDTO.getCvFileName());
        Resource resource = null;

        try {
            resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePath.getFileName().toString() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @Operation(
            summary = "Get open candidates",
            description = "Retrieve a list of candidates with status 'Open'.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved open candidates"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @GetMapping("/getOpenCandidates")
    public ResponseEntity<List<CandidateDTO>> getOpenCandidates() {
        try {
            return ResponseEntity.ok(candidateService.getCandidateByStatus("Open"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @Operation(
            summary = "Get candidates for schedule edit",
            description = "Retrieve candidates who are open for schedule editing.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved candidates"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @GetMapping("/getCandidatesForScheduleEdit")
    public ResponseEntity<List<CandidateDTO>> getCandidatesForScheduleEdit(@RequestParam Long candidateId) {
        try {
            List<CandidateDTO> openCandidates = candidateService.getCandidateByStatus("Open");
            CandidateDTO chosenCandidateDTO = candidateService.findById(candidateId);
            openCandidates.add(chosenCandidateDTO);
            return ResponseEntity.ok(openCandidates);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
