package com.dinhlap.ims.controllers.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController {

    @GetMapping("/error-400")
    public String badRequestPage() {
        return "contents/error/400-error";
    }

    @GetMapping("/error-401")
    public String authorizationRequired() {
        return "contents/error/401-error";
    }

    @GetMapping("/error-403")
    public String forbiddenPage() {
        return "contents/error/403-error";
    }

    @GetMapping("/error-404")
    public String notFoundPage() {
        return "contents/error/404-error";
    }

    @GetMapping("/error-500")
    public String internalServerErrorPage() {
        return "contents/error/500-error";
    }
}
