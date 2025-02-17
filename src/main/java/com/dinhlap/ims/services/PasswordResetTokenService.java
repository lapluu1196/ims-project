package com.dinhlap.ims.services;

import com.dinhlap.ims.entities.PasswordResetToken;
import com.dinhlap.ims.entities.User;

public interface PasswordResetTokenService {

    void save(String token, User user);

    PasswordResetToken findByToken(String token);
}
