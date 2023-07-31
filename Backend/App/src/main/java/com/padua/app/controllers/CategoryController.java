package com.padua.app.controllers;

import com.padua.app.data.dto.CategoryDTO;
import com.padua.app.exceptions.ExceptionResponse;
import com.padua.app.model.entity.Category;
import com.padua.app.services.CategoryService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/category")
public class CategoryController {

    private CategoryService service;

    private ModelMapper modelMapper;
    public CategoryController(CategoryService service, ModelMapper mapper) {
        this.modelMapper = mapper;
        this.service = service;
    }

    @PostMapping(
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE
            },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            })
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDTO create(@RequestBody @Valid CategoryDTO category){

        Category entity = modelMapper.map(category, Category.class);
        entity = service.create(entity);
        return modelMapper.map(entity, CategoryDTO.class);
    }

}
