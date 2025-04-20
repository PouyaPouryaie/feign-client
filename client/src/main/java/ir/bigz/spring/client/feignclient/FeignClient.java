package ir.bigz.spring.client.feignclient;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import ir.bigz.spring.client.OrderResponse;
import ir.bigz.spring.client.ValidationException;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;

@Validated
public interface FeignClient {

    @Valid
    @Headers("Content-Type: application/json")
    @RequestLine("GET /api/v1/order/")
    List<OrderResponse> getOrders();

    @Headers("Content-Type: application/json")
    @RequestLine("GET /api/v1/order/{orderId}")
    OrderResponse getOrderById(@Param("orderId") final long orderId);

    default OrderResponse getOrderByIdValidation(final long orderId) {
        if(orderId <= 0) {
            throw new ValidationException("8000", "orderId must be greater than 0");
        }
        return getOrderById(orderId);
    }
}
