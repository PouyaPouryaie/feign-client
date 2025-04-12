package ir.bigz.spring.client.feignclient;


import lombok.Getter;

import java.io.Serial;

@Getter
public class FeignClientServerException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    private final String errorCode;
    private final int statusCode;

    public FeignClientServerException(int statusCode, String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }


}
