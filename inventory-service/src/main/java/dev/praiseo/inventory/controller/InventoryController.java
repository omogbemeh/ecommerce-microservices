package dev.praiseo.inventory.controller;


import dev.praiseo.inventory.dto.InventoryRequestDTO;
import dev.praiseo.inventory.dto.InventoryResponseDTO;
import dev.praiseo.inventory.model.Inventory;
import dev.praiseo.inventory.repository.InventoryRepository;
import dev.praiseo.inventory.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping(value = "/api/v1/inventory", produces = "application/json")
public class InventoryController {

    private final InventoryService inventoryService;
    private final InventoryRepository inventoryRepository;

    public InventoryController(InventoryService inventoryService,
                               InventoryRepository inventoryRepository) {
        this.inventoryService = inventoryService;
        this.inventoryRepository = inventoryRepository;
    }

    @GetMapping()
    public ResponseEntity<List<InventoryResponseDTO>> getAllInventory() {
        List<InventoryResponseDTO> allInventory = inventoryService.getAllInventory();
        return new ResponseEntity<>(allInventory, HttpStatus.OK);
    }

    @PostMapping (value = "verify")
    public ResponseEntity<Boolean> inStock(@RequestBody List<InventoryRequestDTO> inventoryRequest) {
        Boolean allProductsInStock = inventoryService.checkInStock(inventoryRequest);
        return new ResponseEntity<>(allProductsInStock, HttpStatus.OK);
    }


}
