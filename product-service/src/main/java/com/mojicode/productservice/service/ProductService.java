package com.mojicode.productservice.service;

import com.mojicode.productservice.dto.ProductRequest;
import com.mojicode.productservice.dto.ProductResponse;
import com.mojicode.productservice.model.Product;

import java.util.List;

public interface ProductService {
    void createProduct(ProductRequest productRequest);
    List<ProductResponse> getProducts();
    ProductResponse getProductById(String id);
}