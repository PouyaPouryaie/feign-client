package ir.bigz.spring.client.feignclient;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import ir.bigz.spring.client.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface FeignClient {

    @Valid
    @Headers("Content-Type: application/json")
    @RequestLine("GET /api/v1/order/")
    List<OrderResponse> getOrders();

    @Headers("Content-Type: application/json")
    @RequestLine("GET /api/v1/order/{orderId}")
    OrderResponse getOrderById(@Param("orderId") final long orderId);
}
