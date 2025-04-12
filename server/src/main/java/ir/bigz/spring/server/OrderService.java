package ir.bigz.spring.server;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    public OrderResponse getOrderById(final long orderId) {
        Optional<OrderResponse> first = getOrderResponse().stream()
                .filter(orderResponse -> orderResponse.getOrderId() == orderId)
                .findFirst();
        if (first.isPresent()) {
            return first.get();
        } else {
            throw new ServerException("data not found", "10001");
        }
    }

    public List<OrderResponse> getOrders() {
        return getOrderResponse();
    }

    private List<OrderResponse> getOrderResponse() {
        OrderResponse first = OrderResponse.builder()
                .orderId(1)
                .price(120.0)
                .description("this is first order")
                .build();
        OrderResponse second = OrderResponse.builder()
                .orderId(2)
                .price(130.0)
                .description("this is second order")
                .build();

        return List.of(first, second);
    }
}
