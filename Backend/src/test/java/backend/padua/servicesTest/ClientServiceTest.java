package backend.padua.servicesTest;

import backend.padua.data.dto.ClientDTO;
import backend.padua.model.Address;
import backend.padua.model.Client;
import backend.padua.repositories.ClientRepository;
import backend.padua.services.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ClientServiceTest {

    ClientService service;

    @MockBean
    ClientRepository repository;

    @Spy
    ModelMapper modelMapper;

    @BeforeEach
    public void setUp(){
        this.service = new ClientService(repository, modelMapper);
    }

    @Test
    @DisplayName("Deve criar um cliente com sucesso.")
    public void shouldCreateClientTest(){

    }

    @Test
    @DisplayName("Deve obter uma lista dos clientes.")
    public void shouldFindAllClientsTest(){

        ClientDTO dtoClient1 = ClientDTO.builder().id(1L).name("Client1").email("x@xx.com").build();
        ClientDTO dtoClient2 = ClientDTO.builder().id(2L).name("Client2").email("xx@xx.com").build();
        ClientDTO dtoClient3 = ClientDTO.builder().id(3L).name("Client3").email("xxx@xx.com").build();

        List<ClientDTO> dtoClients = new ArrayList<>();

        dtoClients.add(dtoClient1);
        dtoClients.add(dtoClient2);
        dtoClients.add(dtoClient3);

        Client client1 = Client.builder().id(1L).email("x@xx.com").name("Client1").build();
        Client client2 = Client.builder().id(2L).email("xx@xx.com").name("Client2").build();
        Client client3 = Client.builder().id(3L).email("xxx@xx.com").name("Client3").build();

        List<Client> clients = new ArrayList<>();

        clients.add(client1);
        clients.add(client2);
        clients.add(client3);

        when(repository.findAll()).thenReturn(clients);

        List<ClientDTO> foundClients = service.findAll();

        ClientDTO cli1 = foundClients.get(0);
        ClientDTO cli2 = foundClients.get(1);
        ClientDTO cli3 = foundClients.get(2);

        assertThat( cli1 ).isNotNull();
        assertThat( cli1.getLinks() ).isNotNull();

        assertThat( cli1.getId() ).isEqualTo(dtoClient1.getId());
        assertThat( cli1.getName() ).isEqualTo(dtoClient1.getName());
        assertThat( cli1.getEmail() ).isEqualTo(dtoClient1.getEmail());
        assertThat( cli1.toString().contains("links: [</api/clients/1>;rel=\"self\"]") ).isTrue();

        assertThat( cli2 ).isNotNull();
        assertThat( cli2.getLinks() ).isNotNull();

        assertThat( cli2.getId() ).isEqualTo(dtoClient2.getId());
        assertThat( cli2.getName() ).isEqualTo(dtoClient2.getName());
        assertThat( cli2.getEmail() ).isEqualTo(dtoClient2.getEmail());
        assertThat( cli2.toString().contains("links: [</api/clients/2>;rel=\"self\"]") ).isTrue();

        assertThat( cli3 ).isNotNull();
        assertThat( cli3.getLinks() ).isNotNull();

        assertThat( cli3.getId() ).isEqualTo(dtoClient3.getId());
        assertThat( cli3.getName() ).isEqualTo(dtoClient3.getName());
        assertThat( cli3.getEmail() ).isEqualTo(dtoClient3.getEmail());
        assertThat( cli3.toString().contains("links: [</api/clients/3>;rel=\"self\"]") ).isTrue();
    }

    @Test
    @DisplayName("Deve lançar erro ao buscar uma lista de clientes vazia.")
    public void emptyClientsFindAllTest() {

        Throwable exception = catchThrowable(() ->
                service.findAll());

        String expectedMessage = "No clients found!";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    @DisplayName("Deve encontrar um cliente por Id.")
    public void shouldFindClientByIdTest(){

        Long id = 1L;

        Address ad = Address.builder().id(id).address("Rua").neighborhood("Bairro").number(10)
                .complement(null).city("Cidade").state("Estado").cep("000000000").build();

        List<Address> adList = new ArrayList<>();
        adList.add(ad);

        Set<String> phone = new HashSet<>();
        phone.add("00123456789");

        Client cli = Client.builder().id(id).name("Client").email("x@xx.com")
                .cpf("123456789").addresses(adList).telephones(phone).build();

        ad.setClient(cli);

        when(repository.findById(id)).thenReturn(Optional.of(cli));

        ClientDTO foundClient = service.findById(id);

        assertThat( foundClient ).isNotNull();
        assertThat( foundClient.getLinks() ).isNotNull();

        assertThat( foundClient.getId() ).isEqualTo(id);
        assertThat( foundClient.getName() ).isEqualTo(cli.getName());
        assertThat( foundClient.getEmail() ).isEqualTo(cli.getEmail());
        assertThat( foundClient.toString().contains("links: [</api/clients/1>;rel=\"self\"]") ).isTrue();
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar buscar cliente nulo por Id.")
    public void clientNotFoundByIdTest(){

        Throwable exception = catchThrowable(() ->
                service.findById(null));

        String expectedMessage = "No records found for this id!";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).contains(expectedMessage);
    }
}
