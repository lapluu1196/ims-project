package com.dinhlap.ims.controllers;

import com.dinhlap.ims.dtos.user.UserDTO;
import com.dinhlap.ims.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class GlobalController {

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addUserDetails(Principal principal, Model model) {
        if (principal != null) {

            String username = principal.getName();

            try {
                UserDTO userDTO = userService.findByUsername(username);

                model.addAttribute("currentUser", userDTO);
            } catch (Exception e) {

                model.addAttribute("error", "Unable to load user details.");
            }
        }
    }
}
