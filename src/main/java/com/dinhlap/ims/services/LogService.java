package com.dinhlap.ims.services;

public interface LogService {

    void logAction(String action, String entityType, Long entityId, String description);

}
