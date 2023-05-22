package dev.praiseo.inventory.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InventoryRequestDTO {
    private String skuCode;
    private Long quantity;
}
