package com.mojicode.inventoryservice.controller;

import com.mojicode.inventoryservice.dto.InventoryResponse;
import com.mojicode.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory-app/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam(value = "sku-code") List<String> skuCode) {
        return inventoryService.isInStock(skuCode);
    }
}