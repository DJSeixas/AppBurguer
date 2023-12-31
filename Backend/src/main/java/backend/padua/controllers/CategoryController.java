package backend.padua.controllers;

import backend.padua.data.dto.CategoryDTO;
import backend.padua.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping(
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE
            },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            })
    public CategoryDTO update(@RequestBody CategoryDTO category) {
        return service.update(category);
    }

    @GetMapping(
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            })
    public List<CategoryDTO> findAll(){
        return service.findAll();
    }

    @GetMapping(value ="/{id}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            })
    public CategoryDTO findById(@PathVariable(value = "id") Long id) {
        return service.findById(id);
    }

    @GetMapping(value ="/name/{name}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            })
    public CategoryDTO findByName(@PathVariable(value = "name") String name) {
        return service.findByName(name);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
