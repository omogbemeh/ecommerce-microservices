package dev.praiseo.order.service;

import dev.praiseo.order.dto.*;
import dev.praiseo.order.model.Order;
import dev.praiseo.order.model.OrderLineItem;
import dev.praiseo.order.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClient;

    public OrderService(OrderRepository orderRepository, WebClient.Builder webClient) {
        this.orderRepository = orderRepository;
        this.webClient = webClient;
    }


    public Order createOrder(OrderRequest orderRequest) {
        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItemsRequest()
                .stream()
                .map(this::mapOrderLineItemRequestToOrderLineItem)
                .collect(Collectors.toList());

        List<InventoryRequestDTO> inventoryRequestDTOS = orderLineItems
                .stream()
                .map(lineItem -> InventoryRequestDTO
                        .builder()
                        .skuCode(lineItem.getSkuCode())
                        .quantity(lineItem.getQuantity())
                        .build())
                .toList();

        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItems(orderLineItems)
                .build();

        Boolean allProductsAreInStock = webClient.build()
                .post()
                .uri("http://inventory-service/api/v1/inventory/verify")
                .bodyValue(inventoryRequestDTOS)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (allProductsAreInStock) {
            Order savedOrder = orderRepository.save(order);
            log.info("Saved order with id: {}", savedOrder.getId());
            return order;
        } else throw new RuntimeException("Problem creating order");
    }

    private OrderLineItemResponse mapOrderLineToOrderLineResponse(OrderLineItem orderLineItem) {
        return OrderLineItemResponse.builder()
                .id(orderLineItem.getId())
                .skuCode(orderLineItem.getSkuCode())
                .quantity(orderLineItem.getQuantity())
                .price(orderLineItem.getPrice())
                .build();
    }

    private OrderLineItem mapOrderLineItemRequestToOrderLineItem(OrderLineItemRequest orderLineItemRequest) {
        return OrderLineItem.builder()
                .skuCode(orderLineItemRequest.getSkuCode())
                .price(orderLineItemRequest.getPrice())
                .quantity(orderLineItemRequest.getQuantity())
                .build();
    }
}
