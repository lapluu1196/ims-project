package com.dinhlap.ims.controllers.auth;

import com.dinhlap.ims.utils.JwtUtil;
import com.dinhlap.ims.dtos.user.UserDTO;
import com.dinhlap.ims.services.RefreshTokenService;
import com.dinhlap.ims.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Authentication", description = "APIs for user authentication")
@RequestMapping("/api/auth")
public class AuthApiController {

    final AuthenticationManager authenticationManager;
    final JwtUtil jwtUtil;
    final RefreshTokenService refreshTokenService;
    final UserService userService;

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @Operation(
            summary = "Forgot Password",
            description = "Send a password reset link to the email address",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password reset link sent successfully."),
                    @ApiResponse(responseCode = "400", description = "The Email address doesn't exist. Please try again.")
            }
    )
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

    @Operation(summary = "User Login", description = "Authenticate the user and return tokens.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username,
                        loginRequest.password
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        refreshTokenService.saveRefreshToken(userDetails.getUsername(), refreshToken);

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
    }

    @Operation(summary = "Refresh Token", description = "Get new access token using refresh token.")
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAccessToken(@RequestBody RefreshTokenRequest request) {
        String username = jwtUtil.getUsernameFromToken(request.getRefreshToken());
        String storedRefreshToken = refreshTokenService.getRefreshToken(username);

        if (storedRefreshToken == null || !storedRefreshToken.equals(request.getRefreshToken())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token.");
        }

        UserDetails userDetails = (UserDetails) userService.loadUserByUsername(username);
        String newAccessToken = jwtUtil.generateAccessToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(newAccessToken, request.getRefreshToken()));
    }

    @Operation(summary = "Logout", description = "Logout the user and invalidate the refresh token.")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token, @RequestParam String username) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        long expirationTime = jwtUtil.getExpirationMsFromToken(token);
        if (expirationTime > 0) {
            refreshTokenService.blacklistAccessToken(token, expirationTime);
        }

        refreshTokenService.deleteRefreshToken(username);
        return ResponseEntity.ok("Logout successful.");
    }


    @Data
    static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    static class RefreshTokenRequest {
        private String refreshToken;
    }

    @Data
    static class AuthResponse {
        private String accessToken;
        private String refreshToken;

        public AuthResponse(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }
}
