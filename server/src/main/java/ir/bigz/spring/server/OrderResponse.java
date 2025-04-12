package ir.bigz.spring.server;


import lombok.*;

@Builder
@AllArgsConstructor
@ToString
@Getter
public class OrderResponse {

    private final long orderId;
    private final double price;
    private final String description;
}
