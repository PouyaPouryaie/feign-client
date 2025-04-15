package ir.bigz.spring.client;

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
        return ResponseEntity.status(e.getStatusCode()).body(getErrorResponse(e));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public void handleRuntimeException(RuntimeException ignored) {}

    private ErrorResponse getErrorResponse(FeignClientServerException e) {
        return ErrorResponse.builder()
                .errorCode(Objects.nonNull(e.getErrorCode()) ? e.getErrorCode() : "9999")
                .message(Objects.nonNull(e.getMessage()) ? e.getMessage() : "unknown Server Error").build();
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
