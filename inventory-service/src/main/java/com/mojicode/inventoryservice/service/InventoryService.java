package com.mojicode.inventoryservice.service;

import com.mojicode.inventoryservice.dto.InventoryResponse;
import com.mojicode.inventoryservice.model.Inventory;
import org.springframework.stereotype.Service;

import java.util.List;

public interface InventoryService {
    List<InventoryResponse> isInStock(List<String> skuCode);
}