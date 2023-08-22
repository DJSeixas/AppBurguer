package backend.padua.services;

import backend.padua.controllers.ClientController;
import backend.padua.controllers.ProductController;
import backend.padua.data.dto.ClientDTO;
import backend.padua.data.dto.NewClientDTO;
import backend.padua.data.dto.ProductDTO;
import backend.padua.exceptions.RequiredObjectIsNullException;
import backend.padua.exceptions.ResourceNotFoundException;
import backend.padua.mapperConverters.mapperConverter;
import backend.padua.model.Address;
import backend.padua.model.Client;
import backend.padua.repositories.ClientRepository;
import jakarta.transaction.Transactional;
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

        modelMapper.addConverter(new mapperConverter.ClientToNewDTOconverter());
        modelMapper.addConverter(new mapperConverter.NewClientDTOToClientConverter());
    }

    @Transactional
    public NewClientDTO create(NewClientDTO client) {

        if(client == null) throw new RequiredObjectIsNullException();

        Client entity = modelMapper.map(client, Client.class);

        Address ad = Address.builder().address(client.getAddress()).number(client.getNumber())
                .complement(client.getComplement()).neighborhood(client.getNeighborhood())
                .city(client.getCity()).state(client.getState()).cep(client.getCep()).client(entity)
                .build();

        entity.getAddresses().add(ad);
        entity.getTelephones().add(client.getTelephone());

        repository.save(entity);

        NewClientDTO cli = modelMapper.map(entity, NewClientDTO.class);

        cli.add(linkTo(methodOn(ClientController.class).findById(cli.getId())).withSelfRel());

        return cli;
    }

    public ClientDTO update(ClientDTO client) {

        if(client == null) throw new RequiredObjectIsNullException();

        Client entity = repository.findById(client.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));

        entity.setName(client.getName());
        entity.setEmail(client.getEmail());

        repository.save(entity);

        ClientDTO cli = modelMapper.map(entity, ClientDTO.class);

        cli.add(linkTo(methodOn(ClientController.class).findById(cli.getId())).withSelfRel());


        return cli;
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

    public void delete(Long id) {

        Client cli =  repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));

        repository.delete(cli);
    }
}
