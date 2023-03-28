package com.mojicode.orderservice.service;

import com.mojicode.orderservice.dto.InventoryResponse;
import com.mojicode.orderservice.dto.OrderLineItemsDto;
import com.mojicode.orderservice.dto.OrderRequest;
import com.mojicode.orderservice.event.OrderPlacedEvent;
import com.mojicode.orderservice.exception.NotFoundException;
import com.mojicode.orderservice.model.Order;
import com.mojicode.orderservice.model.OrderLineItems;
import com.mojicode.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public String placeOrder(OrderRequest orderRequest) {
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItems(
                        orderRequest.getOrderLineItems().stream()
                                .map(this::mapToOrderLineItems).toList()
                )
                .build();

        // check if the stock is present in inventory service

        List<String> skuCodes = order.getOrderLineItems().stream()
                .map(orderLineItems -> orderLineItems.getSkuCode()).toList();

        for (String skuCode : skuCodes) {
            System.out.println(skuCode);
        }

        InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                .uri("http://inventory-service/inventory-app/inventory",
                        uriBuilder -> uriBuilder.queryParam("sku-code", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        Boolean productInStock = Arrays.stream(inventoryResponses).allMatch(inventoryResponse -> inventoryResponse.isInStock());

        // the invalid skuCodes will not return any responses from the inventory service
        if (inventoryResponses.length == 0) {
            throw new NotFoundException("Please check the product again...We are unable to find the product :)");
        }

        if (productInStock) {
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
            return "Order Placed Successfully";
        }
        else {
            throw new NotFoundException("Product is not in stock, please try again later");
        }



    }

    public OrderLineItems mapToOrderLineItems(OrderLineItemsDto orderLineItemsDto) {
        return OrderLineItems.builder()
                .skuCode(orderLineItemsDto.getSkuCode())
                .price(orderLineItemsDto.getPrice())
                .quantity(orderLineItemsDto.getQuantity())
                .build();
    }
}