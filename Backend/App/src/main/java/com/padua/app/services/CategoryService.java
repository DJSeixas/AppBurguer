package com.padua.app.services;

import com.padua.app.exceptions.BusinessExceptions;
import com.padua.app.model.entity.Category;
import com.padua.app.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public Category create(Category category) {

        if(repository.existsByName(category.getName())) {
            throw new BusinessExceptions("Nome já cadastrado.");
        }

        return repository.save(category);
    }
}
