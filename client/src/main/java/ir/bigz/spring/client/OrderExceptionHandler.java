package ir.bigz.spring.client;

import ir.bigz.spring.client.feignclient.FeignClient;
import ir.bigz.spring.client.feignclient.FeignClientErrorDecoder;
import ir.bigz.spring.client.feignclient.FeignClientServerException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Objects;

@ControllerAdvice
public class OrderExceptionHandler {

    @ExceptionHandler(FeignClientServerException.class)
    public ResponseEntity<? extends ErrorResponse> handleServerException(FeignClientServerException e) {
        if(Objects.nonNull(e.getStatusCode())) {
            return ResponseEntity.status(e.getStatusCode()).body(getErrorResponse(e));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getErrorResponse(e));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public void handleRuntimeException(RuntimeException e) {}

    private ErrorResponse getErrorResponse(FeignClientServerException e) {
        return ErrorResponse.builder().errorCode(e.getErrorCode()).message(e.getMessage()).build();
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorResponse {
        private String message;
        private String errorCode;
    }

}
