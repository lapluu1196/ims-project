package com.dinhlap.ims.controllers.candidate;

import com.dinhlap.ims.dtos.candidate.CandidateDTO;
import com.dinhlap.ims.dtos.user.UserDTO;
import com.dinhlap.ims.services.CandidateService;
import com.dinhlap.ims.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String candidateList() {
        return "contents/candidate/candidate-list";
    }

    @GetMapping("/detail/{id}")
    public String candidateDetail(@PathVariable("id") Long id) {
        return "contents/candidate/candidate-detail";
    }

    @GetMapping("/edit/{id}")
    public String editCandidate(@PathVariable("id") Long id) {
        return "contents/candidate/candidate-edit";
    }

    @GetMapping("/create")
    public String createCandidate() {
        return "contents/candidate/candidate-create";
    }
}
