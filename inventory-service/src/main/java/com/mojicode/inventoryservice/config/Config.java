package com.mojicode.inventoryservice.config;

import com.mojicode.inventoryservice.model.Inventory;
import com.mojicode.inventoryservice.respository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
        return args -> {
            Inventory inventory = Inventory.builder()
                    .skuCode("i-phone black")
                    .quantity(100)
                    .build();
            Inventory inventory1 = Inventory.builder()
                    .skuCode("i-phone red")
                    .quantity(0)
                    .build();

            inventoryRepository.save(inventory);
            inventoryRepository.save(inventory1);
        };
    }

}