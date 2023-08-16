package backend.padua.controllers;

import backend.padua.data.dto.CategoryDTO;
import backend.padua.data.dto.ClientDTO;
import backend.padua.data.dto.NewClientDTO;
import backend.padua.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/clients")
public class ClientController {

    private ClientService service;

    public ClientController(ClientService service) {
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
    public NewClientDTO create(@RequestBody @Valid NewClientDTO client){
        return service.create(client);
    }

    @PutMapping(
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE
            },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            })
    public ClientDTO update(@RequestBody ClientDTO client) {
        return service.update(client);
    }

    @GetMapping(
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            })
    public List<ClientDTO> findAll(){
        return service.findAll();
    }

    @GetMapping(value ="/{id}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            })
    public ClientDTO findById(@PathVariable(value = "id") Long id) {
        return service.findById(id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
