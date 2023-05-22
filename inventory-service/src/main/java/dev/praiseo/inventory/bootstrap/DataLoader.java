package dev.praiseo.inventory.bootstrap;

import dev.praiseo.inventory.dto.InventoryRequestDTO;
import dev.praiseo.inventory.dto.InventoryResponseDTO;
import dev.praiseo.inventory.model.Inventory;
import dev.praiseo.inventory.service.InventoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader implements CommandLineRunner {

    private final InventoryService inventoryService;

    public DataLoader(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Override
    public void run(String... args) throws Exception {
        InventoryRequestDTO playstation5 = InventoryRequestDTO
                .builder()
                .skuCode("playstation-5")
                .quantity(2L)
                .build();

        InventoryRequestDTO macbookPro16 = InventoryRequestDTO
                .builder()
                .skuCode("macbook-pro-16")
                .quantity(10L)
                .build();

        inventoryService.createInventory(playstation5);
        inventoryService.createInventory(macbookPro16);
    }
}
