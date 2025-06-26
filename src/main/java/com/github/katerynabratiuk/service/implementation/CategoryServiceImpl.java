package com.github.katerynabratiuk.service.implementation;

import com.github.katerynabratiuk.entity.Category;
import com.github.katerynabratiuk.repository.implementation.CategoryRepositoryImpl;
import com.github.katerynabratiuk.service.CategoryService;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepositoryImpl categoryRepository;

    public CategoryServiceImpl(CategoryRepositoryImpl categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAll() {
       return categoryRepository.getAll();
    }

    @Override
    public Category get(Integer id) {
        return categoryRepository.get(id);
    }

    @Override
    public Category create(Category category) {
        if (!category.getName().isBlank())
            return categoryRepository.create(category);
        throw new RuntimeException("Category name cannot be blank");
    }

    @Override
    public void update(Category category) {
        if (!category.getName().isBlank())
            categoryRepository.create(category);
        throw new RuntimeException("Category name cannot be blank");
    }

    @Override
    public void delete(Integer id) {
        categoryRepository.delete(id);
    }
}
