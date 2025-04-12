package ir.bigz.spring.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@AllArgsConstructor
@ToString
@Getter
@NoArgsConstructor
public class OrderResponse {

    @NotNull
    private long orderId;
    private double price;
    @NotNull
    @NotBlank
    private String description;
}
