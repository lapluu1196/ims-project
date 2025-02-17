package com.dinhlap.ims.controllers.auth;

import com.dinhlap.ims.dtos.api.ApiResponse;
import com.dinhlap.ims.dtos.authentication.AuthenticationRequest;
import com.dinhlap.ims.dtos.authentication.AuthenticationResponse;
import com.dinhlap.ims.dtos.authentication.IntrospectRequest;
import com.dinhlap.ims.dtos.authentication.IntrospectResponse;
import com.dinhlap.ims.dtos.user.UserDTO;
import com.dinhlap.ims.services.AuthenticationService;
import com.dinhlap.ims.services.UserService;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/auth")
public class AuthApiController {

    UserService userService;

    AuthenticationService authenticationService;

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @PostMapping("/password/forgot")
    public ResponseEntity<String> forgotPassword(HttpServletRequest request, @RequestParam("email") String email) {

        UserDTO userDTO = userService.findByEmail(email);

        if (userDTO == null) {
            return ResponseEntity.badRequest().body("The Email address doesn't exist. Please try again.");
        }

        String token = UUID.randomUUID().toString();

        String resetUrl = getSiteURL(request) + "/auth/password/reset?token=" + token;

        userService.createPasswordResetTokenForUser(email, resetUrl, token);

        return ResponseEntity.ok("We've sent an email with the link to reset your password.");
    }

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);

        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);

        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }
}
