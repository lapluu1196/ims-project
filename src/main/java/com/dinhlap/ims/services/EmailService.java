package com.dinhlap.ims.services;

import com.dinhlap.ims.dtos.email.EmailDTO;

public interface EmailService {

    String sendEmail(EmailDTO emailDTO, String fileName, String templateName);
}
