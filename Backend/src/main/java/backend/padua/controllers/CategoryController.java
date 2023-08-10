package backend.padua.controllers;

import backend.padua.data.dto.CategoryDTO;
import backend.padua.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/categories")
public class CategoryController {

    private CategoryService service;

    public CategoryController(CategoryService service) {
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
        return service.create(category);
    }
}
