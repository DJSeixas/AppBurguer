package backend.padua.controllers;

import backend.padua.data.dto.ProductDTO;
import backend.padua.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/products")
public class ProductController {

    private ProductService service;

    public ProductController(ProductService service) {
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
    public ProductDTO create(@RequestBody @Valid ProductDTO product){
        return service.create(product);
    }

    @PutMapping(
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE
            },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            })
    public ProductDTO update(@RequestBody ProductDTO product) {
        return service.update(product);
    }

    @GetMapping(
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            })
    public List<ProductDTO> findAll(){
        return service.findAll();
    }

    @GetMapping(value ="/{id}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            })
    public ProductDTO findById(@PathVariable(value = "id") Long id) {
        return service.findById(id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
