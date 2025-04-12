package ir.bigz.spring.client.feignclient;

import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Bean
    public FeignClient feignClient(FeignClientErrorDecoder feignClientErrorDecoder,
                                   FeignClientLogger feignClientLogger) {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logger(feignClientLogger)
                .errorDecoder(feignClientErrorDecoder)
                .logLevel(Logger.Level.FULL)
                .target(FeignClient.class, "http://localhost:9090");
    }
}
