package com.dinhlap.ims.controllers.auth;

import com.dinhlap.ims.dtos.user.UserDTO;
import com.dinhlap.ims.entities.PasswordResetToken;
import com.dinhlap.ims.services.EmailService;
import com.dinhlap.ims.services.PasswordResetTokenService;
import com.dinhlap.ims.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/auth")
public class AuthController {

    UserService userService;

    EmailService emailService;

    PasswordResetTokenService passwordResetTokenService;

    @GetMapping("/login")
    public String login() {

        return "auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }

    @GetMapping("/password/reset")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {

        PasswordResetToken passwordResetToken = passwordResetTokenService.findByToken(token);
        if (passwordResetToken == null) {
            model.addAttribute("message", "Invalid Token");
            return "auth/login";
        }
        if (passwordResetToken.isExpired()) {
            model.addAttribute("message", "Token Expired");
            return "auth/login";
        }

        model.addAttribute("token", token);
        model.addAttribute("userId", passwordResetToken.getUser().getUserId());

        return "auth/reset-password";
    }

    @PostMapping("/password/reset")
    public String processResetPassword(HttpServletRequest request, @RequestParam("token") String token,
                                       @RequestParam("userId") Long userId, @RequestParam("password") String password,
                                       RedirectAttributes redirectAttributes, Model model) {

        PasswordResetToken passwordResetToken = passwordResetTokenService.findByToken(token);
        if (passwordResetToken == null) {
            model.addAttribute("message", "Invalid Token");
            return "auth/reset-password";
        }

        if (passwordResetToken.isExpired()) {
            model.addAttribute("message", "Token Expired");
            return "auth/reset-password";
        }

        UserDTO userDTO = userService.findById(userId);

        if (userDTO == null) {
            redirectAttributes.addFlashAttribute("message", "Account Not Found");
            return "redirect:/auth/login";
        }

        String result = userService.updatePassword(userDTO.getUserId(), password);

        redirectAttributes.addFlashAttribute("message", result);

        return "redirect:/auth/login";
    }

}
