package io.dodn.springboot.member.api.support.error;

public class MemberApiException extends RuntimeException {

    private final ErrorType errorType;

    private final Object data;

    public MemberApiException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.data = null;
    }

    public MemberApiException(ErrorType errorType, Object data) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.data = data;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public Object getData() {
        return data;
    }

}
