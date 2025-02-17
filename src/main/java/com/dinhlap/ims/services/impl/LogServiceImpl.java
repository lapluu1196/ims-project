package com.dinhlap.ims.services.impl;

import com.dinhlap.ims.entities.Log;
import com.dinhlap.ims.entities.User;
import com.dinhlap.ims.repositories.LogRepository;
import com.dinhlap.ims.services.LogService;
import com.dinhlap.ims.utils.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Override
    public void logAction(String action, String entityType, Long entityId, String description) {
        User currentUser = currentUserUtil.getCurrentUser();

        Log log = new Log();
        log.setAction(action);
        log.setUser(currentUser);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setDescription(description);
        log.setTimestamp(LocalDateTime.now());

        logRepository.save(log);
    }
}
