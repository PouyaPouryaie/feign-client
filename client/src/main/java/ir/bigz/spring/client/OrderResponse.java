package ir.bigz.spring.client;

import lombok.*;

@Builder
@AllArgsConstructor
@ToString
@Getter
@NoArgsConstructor
public class OrderResponse {

    private long orderId;
    private double price;
    private String description;
}
