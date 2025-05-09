package ir.bigz.spring.client.feignclient;

import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class FeignClientConfig {

    @Bean
    public FeignClient feignClient(FeignClientErrorDecoder feignClientErrorDecoder,
                                   FeignClientHeaderRequestInterceptor feignClientHeaderRequestInterceptor,
                                   FeignClientLogger feignClientLogger) {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .requestInterceptor(feignClientHeaderRequestInterceptor)
                .logger(feignClientLogger)
                .errorDecoder(feignClientErrorDecoder)
                .logLevel(Logger.Level.FULL)
                .options(new Request.Options(5, TimeUnit.SECONDS, 10, TimeUnit.SECONDS, false))
                .retryer(new FeignClientRetryer(5, 200L, TimeUnit.SECONDS.toMillis(3L)))
                .target(FeignClient.class, "http://localhost:9090");
    }
}
