package backend.padua.controllerTest;

import backend.padua.controllers.ClientController;
import backend.padua.data.dto.CategoryDTO;
import backend.padua.data.dto.ClientDTO;
import backend.padua.data.dto.NewClientDTO;
import backend.padua.data.dto.ProductDTO;
import backend.padua.services.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = ClientController.class)
@AutoConfigureMockMvc
public class ClientControllerTest {

    static final String CLIENT_API = "/api/clients";

    @Autowired
    MockMvc mvc;

    @MockBean
    ClientService service;

    @Test
    @DisplayName("Deve criar um cliente com sucesso.")
    public void shouldCreateClientTest() throws Exception{

        NewClientDTO dto = getNewClientDTO();

        String json = new ObjectMapper().writeValueAsString(dto);

        NewClientDTO savedDto = getNewClientDTO();
        savedDto.setId(1L);

        given(service.create(Mockito.any(NewClientDTO.class))).willReturn(savedDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CLIENT_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform( request )
                .andExpect( status().isCreated() )
                .andExpect( jsonPath("id").isNotEmpty() )
                .andExpect( jsonPath("name").value(dto.getName()))
                .andExpect( jsonPath("email").value(dto.getEmail()))
                .andExpect( jsonPath("cpf").value(dto.getCpf()))
                .andExpect( jsonPath("address").value(dto.getAddress()))
                .andExpect( jsonPath("number").value(dto.getNumber()))
                .andExpect( jsonPath("complement").value(dto.getComplement()))
                .andExpect( jsonPath("neighborhood").value(dto.getNeighborhood()))
                .andExpect( jsonPath("city").value(dto.getCity()))
                .andExpect( jsonPath("state").value(dto.getState()))
                .andExpect( jsonPath("cep").value(dto.getCep()))
                .andExpect( jsonPath("telephone").value(dto.getTelephone()));

    }

    @Test
    @DisplayName("Deve lançar erro de validação quando o cpf for inválido.")
    public void shouldNotCreateClientWithInvalidCPFTest() throws Exception {

        NewClientDTO dto = getNewClientDTO();
        dto.setCpf("00000000000");

        String json = new ObjectMapper().writeValueAsString(dto);

        NewClientDTO savedDto = getNewClientDTO();
        savedDto.setId(1L);

        given(service.create(Mockito.any(NewClientDTO.class))).willReturn(savedDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CLIENT_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform( request )
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath("errorList", hasSize(1)))
                .andExpect( jsonPath("message").value("CPF inválido"));
    }

    @Test
    @DisplayName("Deve lançar erro de validação quando o email for inválido.")
    public void shouldNotCreateClientWithInvalidEmailTest() throws Exception {

        NewClientDTO dto = getNewClientDTO();
        dto.setEmail("xxxxxxxx");

        String json = new ObjectMapper().writeValueAsString(dto);

        NewClientDTO savedDto = getNewClientDTO();
        savedDto.setId(1L);

        given(service.create(Mockito.any(NewClientDTO.class))).willReturn(savedDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CLIENT_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform( request )
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath("errorList", hasSize(1)))
                .andExpect( jsonPath("message").value("Email inválido"));
    }


    @Test
    @DisplayName("Deve lançar erro de validação quando não houver dados suficientes para a criação do cliente.")
    public void createInvalidCategoryTest() throws Exception {

        String json = new ObjectMapper().writeValueAsString(new NewClientDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CLIENT_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform( request )
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath("errorList", hasSize(9)))
                .andExpect( jsonPath("message").value("Preenchimento obrigatório"));
    }

    @Test
    @DisplayName("Deve atualizar um cliente.")
    public void shouldUpdateClientTest() throws Exception{

        Long id = 1L;

        ClientDTO dto = ClientDTO.builder().id(id).name("Client").email("x@xxx.com").build();

        String json = new ObjectMapper().writeValueAsString(dto);

        given(service.findById(Mockito.anyLong())).willReturn(dto);

        ClientDTO updatedClient = ClientDTO.builder().id(id).name("updatedName").email("xx@xxx.com").build();

        given(service.update(dto)).willReturn(updatedClient);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(CLIENT_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform( request )
                .andExpect( status().isOk())
                .andExpect( jsonPath("id").value(id))
                .andExpect( jsonPath("name").value(updatedClient.getName()))
                .andExpect( jsonPath("email").value(updatedClient.getEmail()));
    }

    @Test
    @DisplayName("Deve retornar uma lista dos clientes.")
    public void shouldFindAllClientsTest() throws Exception{

        ClientDTO Client1 = ClientDTO.builder().id(1L).name("Client1").email("x@xxx.com").build();
        ClientDTO Client2 = ClientDTO.builder().id(2L).name("Client2").email("xx@xxx.com").build();
        ClientDTO Client3 = ClientDTO.builder().id(3L).name("Client3").email("xxx@xxx.com").build();

        List<ClientDTO> clients = new ArrayList<>();

        clients.add(Client1);
        clients.add(Client2);
        clients.add(Client3);

        given(service.findAll()).willReturn(clients);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CLIENT_API)
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform( request )
                .andExpect( status().isOk() )
                .andExpect( content().contentType(MediaType.APPLICATION_JSON) )
                .andExpect( jsonPath("$", hasSize(3)) )

                .andExpect( jsonPath("$[0].id").value(Client1.getId()) )
                .andExpect( jsonPath("$[0].name").value(Client1.getName()) )
                .andExpect( jsonPath("$[0].email").value(Client1.getEmail()) )

                .andExpect( jsonPath("$[1].id").value(Client2.getId()) )
                .andExpect( jsonPath("$[1].name").value(Client2.getName()) )
                .andExpect( jsonPath("$[1].email").value(Client2.getEmail()) )

                .andExpect( jsonPath("$[2].id").value(Client3.getId()) )
                .andExpect( jsonPath("$[2].name").value(Client3.getName()) )
                .andExpect( jsonPath("$[2].email").value(Client3.getEmail()) );
    }

    @Test
    @DisplayName("Deve retornar Cliente pelo id.")
    public void shouldFindClientByIdTest() throws Exception {

        Long id = 1L;

        ClientDTO client = ClientDTO.builder().id(1L).name("Client").email("x@xxx.com").build();

        given(service.findById(id)).willReturn(client);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CLIENT_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform( request )
                .andExpect( status().isOk() )

                .andExpect( jsonPath("id").value(client.getId()) )
                .andExpect( jsonPath("name").value(client.getName()) )
                .andExpect( jsonPath("email").value(client.getEmail()) );
    }

    @Test
    @DisplayName("Deve deletar um cliente.")
    public void deleteClientTest() throws Exception{

        given(service.findById(Mockito.anyLong())).willReturn(ClientDTO.builder().id(1L).name("client").email("x@xx.com").build());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(CLIENT_API.concat("/"+ 1));

        mvc
                .perform( request )
                .andExpect( status().isNoContent() );
    }

    private static NewClientDTO getNewClientDTO() {
        return NewClientDTO.builder().name("Client").email("x@xx.com").cpf("77058753067")
                .address("Rua").number(1).complement("complement").neighborhood("Bairro").city("Cidade")
                .state("Estado").cep("10111000").telephone("12912345678").build();
    }
}
