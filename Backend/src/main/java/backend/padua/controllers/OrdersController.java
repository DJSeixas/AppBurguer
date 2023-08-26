package backend.padua.controllers;

import backend.padua.data.dto.OrderDTO;

import backend.padua.services.OrdersService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/orders")
public class OrdersController {

    private OrdersService service;

    public OrdersController(OrdersService service) {
        this.service = service;
    }

    @GetMapping(
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public List<OrderDTO> findAll(){
        return service.findAll();
    }

    @GetMapping(value = "/{id}",
            produces = {
                MediaType.APPLICATION_JSON_VALUE
            })
    public OrderDTO findById(@PathVariable(value = "id") Long id){
        return service.findById(id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id")Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
