package dev.praiseo.inventory.service;

import dev.praiseo.inventory.dto.InventoryRequestDTO;
import dev.praiseo.inventory.dto.InventoryResponseDTO;
import dev.praiseo.inventory.model.Inventory;
import dev.praiseo.inventory.repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.module.ResolutionException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class InventoryService {

    public final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional(readOnly = true)
    public List<InventoryResponseDTO> getAllInventory() {
        List<Inventory> allInventory = this.inventoryRepository.findAll();

        List<InventoryResponseDTO> inventoryResponseList = allInventory.stream().map(product -> InventoryResponseDTO.builder()
                        .id(product.getId())
                        .skuCode(product.getSkuCode())
                        .quantity(product.getQuantity())
                        .build())
                .collect(Collectors.toList());

        return inventoryResponseList;
    }

    @Transactional(readOnly = true)
    public Boolean checkInStock(List<InventoryRequestDTO> inventoryRequest) {
        log.info("Checking for quantity in stock {}", inventoryRequest);
        List<String> skuCodes = getSkuCodesFromRequest(inventoryRequest);
        List<Inventory> inventory = inventoryRepository.findBySkuCodeIn(skuCodes);
        Map<String, Long> skuCodeAndQuantityMap = convertToMap(inventory);
        return checkIfProductsAreInStock(inventoryRequest, skuCodeAndQuantityMap);
    }

    public Inventory createInventory(InventoryRequestDTO inventoryRequestDTO) {
        Inventory inventory = Inventory.builder()
                .skuCode(inventoryRequestDTO.getSkuCode())
                .quantity(inventoryRequestDTO.getQuantity())
                .build();
        Inventory savedInventory = inventoryRepository.save(inventory);
        return savedInventory;
    }

    private List<String> getSkuCodesFromRequest(List<InventoryRequestDTO> inventoryRequest) {
        return inventoryRequest
                .stream()
                .map(InventoryRequestDTO::getSkuCode)
                .toList();
    }

    private Boolean checkIfProductsAreInStock(List<InventoryRequestDTO> inventoryRequest, Map<String, Long> inventorySkuCodeToQuantity) {
        return inventoryRequest
                .stream()
                .allMatch(prod -> prod.getQuantity() <= inventorySkuCodeToQuantity.get(prod.getSkuCode()));
    }

    private Map<String, Long> convertToMap(List<Inventory> inventories) {
        Map<String, Long> skuCodeToQuantityMap = new HashMap<>();
        inventories.stream().forEach(inventory -> skuCodeToQuantityMap
                .put(inventory.getSkuCode(), inventory.getQuantity()));
        return skuCodeToQuantityMap;
    }
}
