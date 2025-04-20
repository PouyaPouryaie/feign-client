package ir.bigz.spring.server;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class OrderExceptionHandler {

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<? extends ErrorResponse> handleServerException(ServerException e) {
        return ResponseEntity.status(e.getStatusCode()).body(getErrorResponse(e));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ErrorResponse handleRuntimeException(RuntimeException e) {
        return getErrorResponse(e);
    }

    private ErrorResponse getErrorResponse(ServerException e) {
        return ErrorResponse.builder().errorCode(e.getErrorCode()).message(e.getMessage()).build();
    }

    private ErrorResponse getErrorResponse(RuntimeException e) {
        return ErrorResponse.builder().errorCode("9999").message(e.getMessage()).build();
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
