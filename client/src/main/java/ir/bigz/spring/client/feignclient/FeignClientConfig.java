package ir.bigz.spring.client.feignclient;

import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.feign.FeignDecorator;
import io.github.resilience4j.feign.FeignDecorators;
import io.github.resilience4j.feign.Resilience4jFeign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.ConnectException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class FeignClientConfig {

//    @Bean
//    public FeignClient feignClient(FeignClientErrorDecoder feignClientErrorDecoder,
//                                   FeignClientHeaderRequestInterceptor feignClientHeaderRequestInterceptor,
//                                   FeignClientLogger feignClientLogger) {
//        return Feign.builder()
//                .encoder(new JacksonEncoder())
//                .decoder(new JacksonDecoder())
//                .requestInterceptor(feignClientHeaderRequestInterceptor)
//                .logger(feignClientLogger)
//                .errorDecoder(feignClientErrorDecoder)
//                .logLevel(Logger.Level.FULL)
//                .retryer(new FeignClientRetryer(5, 200L, TimeUnit.SECONDS.toMillis(3L)))
//                .target(FeignClient.class, "http://localhost:9090");
//    }

    @Bean
    public FeignClient feignClient(FeignClientErrorDecoder feignClientErrorDecoder,
                                   FeignClientHeaderRequestInterceptor feignClientHeaderRequestInterceptor,
                                   FeignClientLogger feignClientLogger) {

        CircuitBreakerConfig circuitBreakerConfig =
                CircuitBreakerConfig.custom()
                        .failureRateThreshold(50)

                        .waitDurationInOpenState(Duration.ofSeconds(10))
                        .permittedNumberOfCallsInHalfOpenState(3)
                        .slidingWindowSize(10)
                        .build();

        CircuitBreaker circuitBreaker = CircuitBreaker.of("feign-client", circuitBreakerConfig);

        FeignDecorators decorators = FeignDecorators.builder()
                .withCircuitBreaker(circuitBreaker)
                .withFallback(new FeignClientFallback())
                .build();


        return Resilience4jFeign.builder(decorators)
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .requestInterceptor(feignClientHeaderRequestInterceptor)
                .logger(feignClientLogger)
                .logLevel(Logger.Level.FULL)
//                .errorDecoder(feignClientErrorDecoder)
                .target(FeignClient.class, "http://localhost:9090");
    }
}
