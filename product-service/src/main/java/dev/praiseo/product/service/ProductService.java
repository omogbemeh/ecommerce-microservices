package dev.praiseo.product.service;

import dev.praiseo.product.dto.ProductRequest;
import dev.praiseo.product.dto.ProductResponse;
import dev.praiseo.product.model.Product;
import dev.praiseo.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse saveProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        Product response = productRepository.insert(product);
        log.info("Saved product with id {}", response.getId());

        return ProductResponse.builder()
                .id(response.getId())
                .name(response.getName())
                .description(response.getDescription())
                .price(response.getPrice())
                .build();

    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        List<ProductResponse> productResponses = products.stream()
                .map(this::mapRoProductResponse).collect(Collectors.toList());

        return productResponses;
    }

    private ProductResponse mapRoProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
