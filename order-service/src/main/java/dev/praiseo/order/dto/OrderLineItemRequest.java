package dev.praiseo.order.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItemRequest {
    private String skuCode;
    private BigDecimal price;
    private Long quantity;
}
