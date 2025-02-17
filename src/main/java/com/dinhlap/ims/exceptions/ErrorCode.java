package com.dinhlap.ims.exceptions;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Error"),
    USER_EXISTED(400, "User already existed"),
    USER_NOT_EXISTED(400, "User not existed"),
    EMAIL_EXISTED(400, "Email already existed"),
    USER_NOT_FOUND(404, "User not found"),
    USER_NOT_AUTHORIZED(403, "User not authorized"),
    UNAUTHENTICATED(401, "Unauthenticated"),
    CANDIDATE_EXISTED(400, "Candidate already existed"),
    CANDIDATE_NOT_FOUND(404, "Candidate not found"),
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private final int code;
    private final String message;

}
