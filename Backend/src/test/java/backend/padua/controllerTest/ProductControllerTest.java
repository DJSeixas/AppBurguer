package backend.padua.controllerTest;

import backend.padua.controllers.ProductController;
import backend.padua.data.dto.ProductDTO;
import backend.padua.exceptions.BusinessException;
import backend.padua.services.ProductService;
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

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc
public class ProductControllerTest {

    static final String PRODUCT_API = "/api/products";

    @Autowired
    MockMvc mvc;

    @MockBean
    ProductService service;

    @Test
    @DisplayName("Deve criar um produto com sucesso.")
    public void createProductTest() throws Exception {

        ProductDTO dto = ProductDTO.builder().name("Product").price(10.00).category("Category").build();

        String json = new ObjectMapper().writeValueAsString(dto);

        ProductDTO savedDTO = ProductDTO.builder().id(1L).name("Product").price(10.00).category("Category").build();

        given(service.create(dto)).willReturn(savedDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(PRODUCT_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform( request )
                .andExpect( status().isCreated() )
                .andExpect( jsonPath("id").isNotEmpty() )
                .andExpect( jsonPath("name").value(dto.getName()) )
                .andExpect( jsonPath( "price" ).value(dto.getPrice()) )
                .andExpect( jsonPath("category").value(dto.getCategory()) );

    }

    @Test
    @DisplayName("Deve lançar erro de validação ao tentar criar produto inválido.")
    public void createInvalidProductTest() throws Exception{

        String json = new ObjectMapper().writeValueAsString(new ProductDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(PRODUCT_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform( request )
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath("errorList", hasSize(3)));
    }

    @Test
    @DisplayName("Deve lançar erro ao criar produto com nome já existente.")
    public void createProductWithDuplicateName() throws Exception{

        ProductDTO dto = ProductDTO.builder().name("Product").price(10.0).category("Category").build();

        String json = new ObjectMapper().writeValueAsString(dto);

        given(service.create(Mockito.any(ProductDTO.class)))
                .willThrow(new BusinessException("Nome já cadastrado."));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(PRODUCT_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform( request )
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath("errorList", hasSize(1)))
                .andExpect( jsonPath("message").value("Nome já cadastrado."));
    }

    @Test
    @DisplayName("Deve atualizar um produto.")
    public void updateProductTest() throws Exception{

        Long id = 1L;

        ProductDTO dto = ProductDTO.builder().id(id).name("Product").build();

        String json = new ObjectMapper().writeValueAsString(dto);

        given(service.findById(Mockito.anyLong()))
                .willReturn(dto);

        ProductDTO updatedProd = ProductDTO.builder().id(id).name("ProductUpdated").build();

        given(service.update(dto))
                .willReturn(updatedProd);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(PRODUCT_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform( request )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("id").value(id) )
                .andExpect( jsonPath("name").value(updatedProd.getName()) )
                .andExpect( jsonPath("price").value(updatedProd.getPrice()) )
                .andExpect( jsonPath("category").value(updatedProd.getCategory()) )
        ;
    }

    @Test
    @DisplayName("Deve retornar uma lista dos produtos.")
    public void findAllProductsTest() throws Exception {

        ProductDTO product1 = ProductDTO.builder().id(1L).name("Product1").build();
        ProductDTO product2 = ProductDTO.builder().id(2L).name("Product2").build();
        ProductDTO product3 = ProductDTO.builder().id(3L).name("Product3").build();

        List<ProductDTO> products = new ArrayList<>();

        products.add(product1);
        products.add(product2);
        products.add(product3);

        given(service.findAll()).willReturn(products);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PRODUCT_API)
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform( request )
                .andExpect( status().isOk() )
                .andExpect( content().contentType(MediaType.APPLICATION_JSON) )
                .andExpect( jsonPath("$", hasSize(3)) )

                .andExpect( jsonPath("$[0].id").value(product1.getId()) )
                .andExpect( jsonPath("$[0].name").value(product1.getName()) )

                .andExpect( jsonPath("$[1].id").value(product2.getId()) )
                .andExpect( jsonPath("$[1].name").value(product2.getName()) )

                .andExpect( jsonPath("$[2].id").value(product3.getId()) )
                .andExpect( jsonPath("$[2].name").value(product3.getName()) );

    }

    @Test
    @DisplayName("Deve buscar um produto por Id com sucesso.")
    public void findProductByIdTest() throws Exception{

        Long id = 1L;

        ProductDTO product = ProductDTO.builder().id(1L).name("Product").price(10.0).category("Category").build();

        given(service.findById(id)).willReturn(product);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PRODUCT_API.concat("/"+ 1))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform( request )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("id").value(id) )
                .andExpect( jsonPath("name").value(product.getName()) )
                .andExpect( jsonPath("price").value(product.getPrice()) )
                .andExpect( jsonPath("category").value(product.getCategory()) )
        ;
    }

    @Test
    @DisplayName("Deve deletar um produto..")
    public void deleteProductTest() throws Exception{

        given(service.findById(Mockito.anyLong())).willReturn(ProductDTO.builder().id(1L).name("Product").price(10.0).category("Category").build());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(PRODUCT_API.concat("/"+ 1));

        mvc
                .perform( request )
                .andExpect( status().isNoContent() );
    }
}
