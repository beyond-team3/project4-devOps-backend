package com.aespa.armageddon.core.common.support.error;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public enum ErrorType {

    // 기본 에러 발생
    DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500, "An unexpected error has occurred.", LogLevel.ERROR),

    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, ErrorCode.C002, "Invalid input value.", LogLevel.WARN),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, ErrorCode.C003, "HTTP method not supported.", LogLevel.WARN),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, ErrorCode.C004, "Invalid type value.", LogLevel.WARN),
    MISSING_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, ErrorCode.C005, "Required request parameter is missing.", LogLevel.WARN),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.U001, "User not found.", LogLevel.WARN),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, ErrorCode.U002, "Email is already in use.", LogLevel.WARN),
    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, ErrorCode.U003, "Login ID is already in use.", LogLevel.WARN),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, ErrorCode.U004, "Password is invalid.", LogLevel.WARN),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, ErrorCode.A001, "Authentication required.", LogLevel.WARN),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, ErrorCode.A002, "Access is denied.", LogLevel.WARN),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, ErrorCode.A003, "Invalid credentials.", LogLevel.WARN),
    SESSION_EXPIRED(HttpStatus.UNAUTHORIZED, ErrorCode.A004, "Session expired.", LogLevel.WARN),
    INVALID_PASSWORD_RESET_CODE(HttpStatus.BAD_REQUEST, ErrorCode.A005, "Password reset code is invalid or expired.", LogLevel.WARN),

    ;

    private final HttpStatus status;

    private final ErrorCode code;

    private final String message;

    private final LogLevel logLevel;

    ErrorType(HttpStatus status, ErrorCode code, String message, LogLevel logLevel) {

        this.status = status;
        this.code = code;
        this.message = message;
        this.logLevel = logLevel;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public ErrorCode getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

}
