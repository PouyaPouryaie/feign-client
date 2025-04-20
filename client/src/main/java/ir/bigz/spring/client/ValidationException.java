package ir.bigz.spring.client;

import lombok.Getter;

import java.io.Serial;

@Getter
public class ValidationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2L;
    private final String errorCode;

    public ValidationException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }
}
