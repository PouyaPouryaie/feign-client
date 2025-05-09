package ir.bigz.spring.client.feignclient;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class FeignClientHeaderRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("X-Client-Requester", "client");
        requestTemplate.header("X-UUID", UUID.randomUUID().toString());
        // You can add more headers or logic here based on the request
    }
}
