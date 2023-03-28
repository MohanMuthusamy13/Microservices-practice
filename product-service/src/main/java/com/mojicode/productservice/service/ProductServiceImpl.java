package com.mojicode.productservice.service;

import com.mojicode.productservice.dto.ProductRequest;
import com.mojicode.productservice.dto.ProductResponse;
import com.mojicode.productservice.model.Product;
import com.mojicode.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
        log.info("Product {} saved successfully", product.getId());
    }

    public ProductResponse getProductById(String id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Product not found")
        );
        ProductResponse productResponse = mapToProductModel(product);
        return productResponse;
    }

    @Override
    public List<ProductResponse> getProducts() {
        List<Product> products =  productRepository.findAll();

        return products.stream().map(this::mapToProductModel).toList();
    }

    public ProductResponse mapToProductModel(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}