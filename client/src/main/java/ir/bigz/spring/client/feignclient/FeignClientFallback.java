package ir.bigz.spring.client.feignclient;

import feign.FeignException;
import ir.bigz.spring.client.OrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class FeignClientFallback implements FeignClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(FeignClientFallback.class);

    private Exception cause;

    public FeignClientFallback() {
    }

//    @Override
//    public List<OrderResponse> getOrders() {
//        if (cause instanceof FeignException) {
//            LOGGER.warn("FeignException: {}", cause.getMessage());
//        } else {
//            LOGGER.warn("Other Exception: {}", cause.getMessage());
//        }
//        return List.of();
//    }

    @Override
    public List<OrderResponse> getOrders() {
        return List.of();
    }

    @Override
    public OrderResponse getOrderById(long orderId) {
        return OrderResponse.builder().orderId(0).description("this is fault").build();
    }
}
