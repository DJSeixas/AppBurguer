package com.padua.app.controllers;

import com.padua.app.data.dto.CategoryDTO;
import com.padua.app.exceptions.ResourceNotFoundException;
import com.padua.app.model.entity.Category;
import com.padua.app.services.CategoryService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

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

    @GetMapping(value ="/{id}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            })
    public CategoryDTO findById(@PathVariable(value="id") Long id) {

        return service.findById(id)
                .map( cat -> modelMapper.map(cat, CategoryDTO.class) )
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping(value ="/{id}",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE
            },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            })
    public CategoryDTO update(@PathVariable(value="id") Long id, CategoryDTO category) {

        return service.findById(id).map( cat -> {
            cat.setName(category.getName());
            cat = service.update(cat);
            return modelMapper.map(cat, CategoryDTO.class);
        })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value="id") Long id){
        Category cat = service.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        service.delete(cat);
    }

}
