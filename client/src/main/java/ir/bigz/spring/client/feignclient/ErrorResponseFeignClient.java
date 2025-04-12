package ir.bigz.spring.client.feignclient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder(toBuilder = true)
@AllArgsConstructor
public class ErrorResponseFeignClient {

    @JsonProperty("message")
    private String message;
    @JsonProperty("errorCode")
    private String errorCode;
    @JsonProperty("status")
    private int status;
    private String timestamp;
    private String error;
    private String path;
}
