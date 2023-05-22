package dev.praiseo.inventory.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InventoryResponseDTO {
    private Long id;
    private String skuCode;
    private Long quantity;
}
