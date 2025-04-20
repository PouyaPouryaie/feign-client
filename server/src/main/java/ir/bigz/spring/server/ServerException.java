package ir.bigz.spring.server;

import lombok.Getter;

@Getter
public class ServerException extends RuntimeException {

    private final int statusCode;
    private final String errorCode;

    public ServerException(int statusCode, String errorCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }
}
