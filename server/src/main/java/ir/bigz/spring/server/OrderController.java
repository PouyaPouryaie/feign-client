package ir.bigz.spring.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final static Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final ServerHeaderHolder serverHeaderHolder;

    public OrderController(OrderService orderService, ServerHeaderHolder serverHeaderHolder) {
        this.orderService = orderService;
        this.serverHeaderHolder = serverHeaderHolder;
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllOrders() {

        logBeforeRequest("getAllOrders");

        List<OrderResponse> orders = orderService.getOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable Integer orderId) {

        logBeforeRequest("getOrder");

        OrderResponse order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    private void logBeforeRequest(String methodName) {
        log.info("Order Controller method: {}, X-UUID: {}", methodName, serverHeaderHolder.getXClientUUID());
    }
}
