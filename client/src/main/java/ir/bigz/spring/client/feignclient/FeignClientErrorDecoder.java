package ir.bigz.spring.client.feignclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public class FeignClientErrorDecoder implements ErrorDecoder {

    private final static Set<Integer> ERROR_STATUS_CODES = new HashSet<>(List.of(400, 401, 402, 405, 500));
    private final ObjectMapper objectMapper;

    public FeignClientErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String s, Response response) {
        try {
            ErrorResponseFeignClient basicErrorResponse = objectMapper
                    .readValue(response.body().asInputStream(), ErrorResponseFeignClient.class);

            final ErrorResponseFeignClient errorResponse = basicErrorResponse.toBuilder().status(response.status()).build();

            throw new FeignClientServerException(errorResponse.getStatus(),
                    Objects.nonNull(errorResponse.getMessage()) ? errorResponse.getMessage() : Objects.nonNull(errorResponse.getError()) ? errorResponse.getError() : "Feign Client Unknown Error",
                    Objects.nonNull(errorResponse.getErrorCode()) ? errorResponse.getErrorCode() : "9000");
        } catch (IOException ex) {
            throw new RuntimeException("Failed to decode error response", ex);
        }
    }

    private boolean checkStatusCode(int statusCode) { return (ERROR_STATUS_CODES.contains(statusCode)); }

}
