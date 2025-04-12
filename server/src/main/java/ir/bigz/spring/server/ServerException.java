package ir.bigz.spring.server;

import lombok.Getter;

@Getter
public class ServerException extends RuntimeException {

    private final String errorCode;

    public ServerException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
