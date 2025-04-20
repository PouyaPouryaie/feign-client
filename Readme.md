# Programmatic FeignClient Implementation in Spring (feign-client)

This repository demonstrates how to programmatically implement a Feign client in a Spring Boot application (`client`) to interact with an external service (`server`). This approach provides fine-grained control over the Feign client's configuration.

## Features

This example showcases the following customizations for the Feign client in the `client` project:

1.  **Customized Request Interceptor:** Demonstrates how to add custom headers or modify requests before they are sent to the `server` external service.
2.  **Customized Logger:** Shows how to implement a custom logger that extends Feign's `Logger` for tailored logging behavior during communication with the `server`.
3.  **Customized Error Decoder:** Illustrates how to implement Feign's `ErrorDecoder` to handle different error responses from the `server`.
4.  **Customized Retry Strategy:** Provides an example of implementing Feign's `Retryer` interface to define a specific retry mechanism for failed requests to the `server`.
5.  **Extra:**
    - **Advantage Of Interface**: We can use interface futures such as default methods. A default method allows us to add some logic to a call method. please check the default method of `FeignClient` class
    - **Timeouts**: Some API calls can take a long time to respond. To prevent the application stuck waiting for the response a good practice is to interrupt such a call after some defined duration. please check the default method of `FeignClientConfig` class

## Repository Structure

The repository `feign-client` contains two sub-projects:

-   **`server`:** A simple Spring Boot application that acts as the external service. It provides basic endpoints for demonstration purposes.
-   **`client`:** A Spring Boot application that implements the Feign client programmatically to communicate with the `server`.

## Version of Tools and Frameworks

-   **Spring Boot:** 3.4.4
-   **Feign Client:** 13.6
-   **Java:** 21

## Prerequisites

-   **Java Development Kit (JDK) 21** or higher installed on your system.
-   **Maven** (3.6.x or higher) for building both the `server` and `client` projects.

## Getting Started

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/PouyaPouryaie/feign-client
    cd feign-client
    ```

2.  **Build the Projects:**
    ```bash
    ./mvnw clean install
    ```

3.  **Run the `server` Application:**
    ```bash
    cd server
    ./mvnw spring-boot:run
    ```
    The `server` application will start on port `9090` (default).

4.  **Run the `client` Application:**
    Open a new terminal and navigate to the `client` directory:
    ```bash
    cd ../client
    ./mvnw spring-boot:run
    ```
    The `client` application will start on port `8080` and attempt to communicate with the `server` using the programmatically configured Feign client.

## Code Highlights (within the `client` project)

### 1. Customized Request Interceptor (`client/src/main/java/ir/bigz/spring/client/feignclient/FeignClientHeaderRequestInterceptor.java`)

This class implements `RequestInterceptor` to add a custom header (`X-Custom-Header: CustomValue`) to every outgoing request made by the Feign client to the `server`.

```java
// client/src/main/java/ir/bigz/spring/client/feignclient/FeignClientHeaderRequestInterceptor.java
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class FeignClientHeaderRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("X-Client-Requester", "client");
        // You can add more headers or logic here based on the request
    }
}
```

### 2. Customized Logger (`client/src/main/java/ir/bigz/spring/client/feignclient/FeignClientLogger.java`)

This class extends Feign's Logger to provide a custom logging implementation using SLF4j. It logs the full request and response information.

### 3. Customized Error Decoder (`client/src/main/java/ir/bigz/spring/client/feignclient/FeignClientErrorDecoder.java`)

This class implements `ErrorDecoder` to handle specific HTTP status codes from the server. In this example, it returns a RetryableException for 500 or above status for retry.

```java
// client/src/main/java/ir/bigz/spring/client/feignclient/FeignClientErrorDecoder.java
@Component
public class FeignClientErrorDecoder implements ErrorDecoder {
    
    //constructor and attributes

    @Override
    public Exception decode(String s, Response response) {
        try {
            ErrorResponseFeignClient basicErrorResponse = objectMapper
                    .readValue(response.body().asInputStream(), ErrorResponseFeignClient.class);
            
            // to create a RetryableException for retrying a request
            if(errorResponse.getStatus() >= 500) {
                return new RetryableException(errorResponse.getStatus(),
                        errorResponse.getMessage(),
                        response.request().httpMethod(),
                        0L,
                        response.request());
            }
            
            // rest of the code
        } catch (IOException ex) {
            throw new RuntimeException("Failed to decode error response", ex);
        }
    }
}
```

### 4. Customized Retry Strategy (`client/src/main/java/ir/bigz/spring/client/feignclient/FeignClientRetryer.java`)

This class implements `feign.Retryer` to define a custom retry strategy with a maximum of 5 attempts.

```java
// client/src/main/java/ir/bigz/spring/client/feignclient/FeignClientRetryer.java
public class FeignClientRetryer implements feign.Retryer {
    
    // constructor and attributes

    @Override
    public void continueOrPropagate(RetryableException e) {

        if(this.attempt++ > this.maxAttempts) {
            logger.error("ERROR: Exceeded Retrying after {} attempts!", this.maxAttempts);
            throw new FeignClientServerException(500,
                    "Failed to connect to the server. Please try again later.",
                    "90001");
        } else {
            long interval;
            
            // implement logic to set interval value

            try {
                Thread.sleep(interval);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw e;
            }
        }
    }

    @Override
    public Retryer clone() {
        return new FeignClientRetryer(maxAttempts, period, maxPeriod);
    }
    
    // rest of the code
}
```
### 5. Programmatic Feign Client Configuration (`client/src/main/java/ir/bigz/spring/client/feignclient/FeignClientConfig.java`)

This class demonstrates how to build the Feign client programmatically using Feign.builder() and injecting the custom components (FeignClientHeaderRequestInterceptor, FeignClientLogger, FeignClientErrorDecoder, FeignClientRetryer).

```Java
// client/src/main/java/ir/bigz/spring/client/feignclient/FeignClientConfig.java
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
```

### Contributing

We welcome contributions to this project. Feel free to fork the repository, make changes, and submit pull requests.