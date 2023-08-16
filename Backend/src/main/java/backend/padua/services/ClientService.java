package backend.padua.services;

import backend.padua.controllers.ClientController;
import backend.padua.controllers.ProductController;
import backend.padua.data.dto.ClientDTO;
import backend.padua.data.dto.NewClientDTO;
import backend.padua.data.dto.ProductDTO;
import backend.padua.exceptions.ResourceNotFoundException;
import backend.padua.repositories.ClientRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

@Service
public class ClientService {

    private ClientRepository repository;

    private ModelMapper modelMapper;

    public ClientService(ClientRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public List<ClientDTO> findAll() {
        List<ClientDTO> clients = modelMapper.map(repository.findAll(), new TypeToken<List<ClientDTO>>() {}.getType());

        if(clients.isEmpty()){
            throw new ResourceNotFoundException("No clients found!");
        }

        clients
                .stream()
                .forEach(c -> c.add(linkTo(methodOn(ClientController.class).findById(c.getId())).withSelfRel()));

        return clients;
    }

    public ClientDTO findById(Long id) {

        var entity = repository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));

        ClientDTO cli = modelMapper.map(entity, ClientDTO.class);

        cli.add(linkTo(methodOn(ClientController.class).findById(cli.getId())).withSelfRel());

        return cli;
    }

    public NewClientDTO create(NewClientDTO any) {
        return null;
    }

    public ClientDTO update(ClientDTO dto) {
        return null;
    }

    public void delete(Long id) {
    }
}
