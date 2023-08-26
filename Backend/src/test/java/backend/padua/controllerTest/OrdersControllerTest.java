package backend.padua.controllerTest;

import backend.padua.controllers.OrdersController;
import backend.padua.data.dto.OrderDTO;
import backend.padua.services.OrdersService;
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
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = OrdersController.class)
@AutoConfigureMockMvc
public class OrdersControllerTest {

    static final String ORDER_API = "/api/orders";

    @Autowired
    MockMvc mvc;

    @MockBean
    OrdersService service;

    @Test
    @DisplayName("Deve criar um pedido com sucesso.")
    public void shouldCreateOrderTest() throws Exception {

    }

    @Test
    @DisplayName("Deve retornar uma lista de pedidos.")
    public void shouldFindAllOrdersTest() throws Exception {

        List<String> itens = new ArrayList<>();

        itens.add("Lanche");
        itens.add("Bebida");

        OrderDTO order1 = OrderDTO.builder().id(1L).date("24/8/2023").clientName("Client1")
                .deliveryAddress("Rua x numero 01").itens(itens).build();
        OrderDTO order2 = OrderDTO.builder().id(2L).date("24/8/2023").clientName("Client2")
                .deliveryAddress("Rua xx numero 02").itens(itens).build();
        OrderDTO order3 = OrderDTO.builder().id(3L).date("24/8/2023").clientName("Client3")
                .deliveryAddress("Rua xxx numero 03").itens(itens).build();

        List<OrderDTO> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);

        given(service.findAll()).willReturn(orders);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(ORDER_API)
                .accept(MediaType.APPLICATION_JSON);
        mvc
                .perform( request )
                .andExpect( status().isOk() )
                .andExpect( content().contentType(MediaType.APPLICATION_JSON) )
                .andExpect( jsonPath("$", hasSize(3)) )

                .andExpect( jsonPath("$[0].id").value(order1.getId()) )
                .andExpect( jsonPath("$[0].date").value(order1.getDate()) )
                .andExpect( jsonPath("$[0].clientName").value(order1.getClientName()) )
                .andExpect( jsonPath("$[0].deliveryAddress").value(order1.getDeliveryAddress()) )
                .andExpect( jsonPath("$[0].itens").value(order1.getItens()) )

                .andExpect( jsonPath("$[1].id").value(order2.getId()) )
                .andExpect( jsonPath("$[1].date").value(order2.getDate()) )
                .andExpect( jsonPath("$[1].clientName").value(order2.getClientName()) )
                .andExpect( jsonPath("$[1].deliveryAddress").value(order2.getDeliveryAddress()) )
                .andExpect( jsonPath("$[1].itens").value(order2.getItens()) )

                .andExpect( jsonPath("$[2].id").value(order3.getId()) )
                .andExpect( jsonPath("$[2].date").value(order3.getDate()) )
                .andExpect( jsonPath("$[2].clientName").value(order3.getClientName()) )
                .andExpect( jsonPath("$[2].deliveryAddress").value(order3.getDeliveryAddress()) )
                .andExpect( jsonPath("$[2].itens").value(order3.getItens()) );
    }

    @Test
    @DisplayName("Deve retornar um pedido pelo id")
    public void shouldFindOrderById() throws Exception {

        Long id = 1L;

        List<String> itens = new ArrayList<>();

        itens.add("Lanche");
        itens.add("Bebida");

        OrderDTO dto = OrderDTO.builder().id(1L).date("24/8/2023").clientName("Client")
                .deliveryAddress("Rua xxx numero 00").itens(itens).build();

        given(service.findById(id)).willReturn(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(ORDER_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform( request )
                .andExpect( status().isOk())

                .andExpect( jsonPath("id").value(dto.getId()))
                .andExpect( jsonPath("date").value(dto.getDate()))
                .andExpect( jsonPath("clientName").value(dto.getClientName()))
                .andExpect( jsonPath("itens").value(dto.getItens()));
    }

    @Test
    @DisplayName("Deve deletar um pedido.")
    public void shouldDeleteOrdersTest() throws Exception{

        List<String> itens = new ArrayList<>();

        itens.add("Lanche");
        itens.add("Bebida");

        OrderDTO dto = OrderDTO.builder().id(1L).date("24/8/2023").clientName("Client")
                .deliveryAddress("Rua xxx numero 00").itens(itens).build();

        given(service.findById(Mockito.anyLong())).willReturn(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(ORDER_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform( request )
                .andExpect( status().isNoContent());
    }
}
