package org.example.ecommerce.Services;

import org.example.ecommerce.dto.product.ProductRequest;
import org.example.ecommerce.dto.product.ProductResponse;
import org.example.ecommerce.entity.Category;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.exception.ResourceNotFoundException;
import org.example.ecommerce.Repositories.CategoryRepository;
import org.example.ecommerce.Repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<ProductResponse> getAll() {
        return productRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec l'id " + id));
        return toResponse(product);
    }

    public List<ProductResponse> getByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ProductResponse> search(String keyword) {
        return productRepository.searchByName(keyword).stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductResponse create(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie non trouvée avec l'id " + request.getCategoryId()));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .imageUrl(request.getImageUrl())
                .category(category)
                .build();

        return toResponse(productRepository.save(product));
    }

    public ProductResponse update(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec l'id " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie non trouvée avec l'id " + request.getCategoryId()));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        return toResponse(productRepository.save(product));
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produit non trouvé avec l'id " + id);
        }
        productRepository.deleteById(id);
    }

    private ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .imageUrl(product.getImageUrl())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .build();
    }
}