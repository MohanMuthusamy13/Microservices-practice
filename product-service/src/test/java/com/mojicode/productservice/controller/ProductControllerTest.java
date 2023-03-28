package com.mojicode.productservice.controller;

import com.mojicode.productservice.dto.ProductRequest;
import com.mojicode.productservice.dto.ProductResponse;
import com.mojicode.productservice.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectWriter;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest {

    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();
    ObjectWriter writer = mapper.writer();

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    ProductRequest productRequest = ProductRequest.builder()
            .name("Fogg Perfume")
            .description("Intense Fragrance Provider")
            .price(BigDecimal.valueOf(999L))
            .build();

    ProductResponse product1 = ProductResponse.builder()
            .id("1")
            .name("Water Bottle")
            .description("Stays heat and cool")
            .price(BigDecimal.valueOf(100L))
            .build();

    ProductResponse product2 = ProductResponse.builder()
            .id("2")
            .name("Sky Bag")
            .description("Makes you stylish")
            .price(BigDecimal.valueOf(200L))
            .build();

    List<ProductResponse> products = List.of(product1, product2);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void createProduct_success() throws Exception {
        Mockito.doNothing().when(productService).createProduct(productRequest);

        String content = writer.writeValueAsString(productRequest);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/product-app/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest).andExpect(status().isCreated());
    }

    @Test
    void getProducts_success() throws Exception {
        Mockito.when(productService.getProducts()).thenReturn(products);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get("/product-app/api/products");

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Water Bottle")));
    }

}