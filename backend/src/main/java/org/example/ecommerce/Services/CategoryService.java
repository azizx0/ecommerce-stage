package org.example.ecommerce.Services;

import org.example.ecommerce.dto.category.CategoryRequest;
import org.example.ecommerce.dto.category.CategoryResponse;
import org.example.ecommerce.entity.Category;
import org.example.ecommerce.exception.BadRequestException;
import org.example.ecommerce.exception.ResourceNotFoundException;
import org.example.ecommerce.Repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public CategoryResponse getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie non trouvée avec l'id " + id));
        return toResponse(category);
    }

    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new BadRequestException("Une catégorie avec ce nom existe déjà");
        }

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        return toResponse(categoryRepository.save(category));
    }

    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie non trouvée avec l'id " + id));

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        return toResponse(categoryRepository.save(category));
    }

    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Catégorie non trouvée avec l'id " + id);
        }
        categoryRepository.deleteById(id);
    }

    private CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}