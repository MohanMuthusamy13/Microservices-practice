package com.mojicode.orderservice.service;

import com.mojicode.orderservice.dto.OrderRequest;

public interface OrderService {
    String placeOrder(OrderRequest orderRequest);
}