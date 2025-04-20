package ir.bigz.spring.client;

import ir.bigz.spring.client.feignclient.FeignClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final FeignClient feignClient;

    public OrderService(FeignClient feignClient) {
        this.feignClient = feignClient;
    }

    public List<OrderResponse> getOrders() {
        return feignClient.getOrders();
    }

    public OrderResponse getOrderById(long orderId) {
        return feignClient.getOrderByIdValidation(orderId);
    }
}
