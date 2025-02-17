package com.dinhlap.ims.controllers.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class AccessDenied {

    @GetMapping("/access-denied")
    public String accessDenied() {

        return "contents/error/access-denied";
    }
}
