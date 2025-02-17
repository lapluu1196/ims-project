package com.dinhlap.ims.services.impl;

import com.dinhlap.ims.entities.PasswordResetToken;
import com.dinhlap.ims.entities.User;
import com.dinhlap.ims.repositories.PasswordResetTokenRepository;
import com.dinhlap.ims.services.PasswordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public void save(String token, User user) {

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiryDate(LocalDateTime.now());

        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public PasswordResetToken findByToken(String token) {

        return passwordResetTokenRepository.findByToken(token);
    }
}
