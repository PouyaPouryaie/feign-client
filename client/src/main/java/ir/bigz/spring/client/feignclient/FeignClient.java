package ir.bigz.spring.client.feignclient;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import ir.bigz.spring.client.OrderResponse;

import java.util.List;

public interface FeignClient {

    @Headers("Content-Type: application/json")
    @RequestLine("GET /api/v1/order/")
    List<OrderResponse> getOrders();

    @Headers("Content-Type: application/json")
    @RequestLine("GET /api/v1/order/{orderId}")
    OrderResponse getOrderById(@Param("orderId") final long orderId);
}
