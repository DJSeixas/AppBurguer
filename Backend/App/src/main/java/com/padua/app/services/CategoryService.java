package com.padua.app.services;

import com.padua.app.data.dto.CategoryDTO;
import com.padua.app.exceptions.BusinessExceptions;
import com.padua.app.model.entity.Category;
import com.padua.app.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {

    private CategoryRepository repository;

    private ModelMapper mapper;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public Category create(Category category) {

        if(repository.existsByName(category.getName())) {
            throw new BusinessExceptions("Nome já cadastrado.");
        }

        return repository.save(category);
    }

    public Optional<Category> findById(Long id) {

        return repository.findById(id);
    }

    public void delete(Category cat) {
        if(cat == null || cat.getId() == null) {
            throw new IllegalArgumentException("Category id cant be null.");
        }

        repository.delete(cat);
    }

    public Category update(Category cat) {
        if(cat == null || cat.getId() == null) {
            throw new IllegalArgumentException("Category id cant be null.");
        }

        return repository.save(cat);
    }
}
