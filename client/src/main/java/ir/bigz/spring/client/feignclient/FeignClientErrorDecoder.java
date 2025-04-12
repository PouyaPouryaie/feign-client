package ir.bigz.spring.client.feignclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class FeignClientErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();
    private final static Set<Integer> ERROR_STATUS_CODES = new HashSet<>(List.of(400, 401, 402, 405, 500));
    private final ObjectMapper objectMapper;

    public FeignClientErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String s, Response response) {

        if (response.status() == 405) {
            try {
                ErrorResponseFeignClient errorResponse = objectMapper
                        .readValue(response.body().asInputStream(), ErrorResponseFeignClient.class);

                throw new FeignClientServerException(errorResponse.getStatus(), errorResponse.getMessage(), errorResponse.getErrorCode());
            } catch (IOException ex) {
                throw new RuntimeException("Failed to decode error response", ex);
            }
        }

        else if (response.status() > 399 && response.status() <= 500) {

            try {
                ErrorResponseFeignClient errorResponse = objectMapper
                        .readValue(response.body().asInputStream(), ErrorResponseFeignClient.class);

                final ErrorResponseFeignClient finalErrorResponse = errorResponse.toBuilder().status(response.status()).build();

                throw new FeignClientServerException(finalErrorResponse.getStatus(), finalErrorResponse.getMessage(), finalErrorResponse.getErrorCode());
            } catch (IOException ex) {
                throw new RuntimeException("Failed to decode error response", ex);
            }
        }

        return defaultDecoder.decode(s, response);
    }

    private boolean checkStatusCode(int statusCode) { return (ERROR_STATUS_CODES.contains(statusCode)); }

}
