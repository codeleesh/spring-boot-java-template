package io.dodn.springboot.core.api.support.error;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public enum ErrorType {

    DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500, "An unexpected error has occurred.",
            LogLevel.ERROR),
    WITHDRAWAL_VALIDATION_FAILED(HttpStatus.CONFLICT, ErrorCode.E409, "Withdrawal validation failed.",
            LogLevel.WARN),
    WITHDRAWAL_WEBHOOK_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.E404, "Webhook registration not found.",
            LogLevel.WARN),
    WITHDRAWAL_WEBHOOK_DUPLICATE(HttpStatus.CONFLICT, ErrorCode.E409,
            "Webhook with same system name already exists.", LogLevel.WARN);

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
