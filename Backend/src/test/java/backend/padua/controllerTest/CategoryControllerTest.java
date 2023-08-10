package backend.padua.controllerTest;

import backend.padua.controllers.CategoryController;
import backend.padua.exceptions.BusinessException;
import backend.padua.model.Category;
import com.fasterxml.jackson.databind.ObjectMapper;
import backend.padua.data.dto.CategoryDTO;
import org.hamcrest.Matchers;
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
import backend.padua.services.CategoryService;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = CategoryController.class)
@AutoConfigureMockMvc
public class CategoryControllerTest {

    static final String CATEGORY_API = "/api/categories";

    @Autowired
    MockMvc mvc;

    @MockBean
    private CategoryService service;

    @Test
    @DisplayName("Deve criar uma categoria com sucesso.")
    public void createCategoryTest() throws Exception {

        CategoryDTO dto = CategoryDTO.builder().name("Category").build();

        String json = new ObjectMapper().writeValueAsString(dto);

        CategoryDTO savedDto = CategoryDTO.builder().id(1L).name("Category").build();


        given(service.create(Mockito.any(CategoryDTO.class))).willReturn(savedDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CATEGORY_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);


        mvc
                .perform(request)
                .andExpect( status().isCreated() )
                .andExpect( jsonPath("id").isNotEmpty() )
                .andExpect( jsonPath("name").value(dto.getName()) )
        ;
    }

    @Test
    @DisplayName("Deve lançar erro de validação quando não houver dados sufucientes para a criação do livro")
    public void createInvalidCategoryTest() throws Exception {

        String json = new ObjectMapper().writeValueAsString(new CategoryDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CATEGORY_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform( request )
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath("errorList", hasSize(1)))
                .andExpect( jsonPath("message").value("Preenchimento obrigatório"));
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar criar categoria com nome já existente.")
    public void CreateCategoryWithDuplicateName() throws Exception{

        CategoryDTO dto = CategoryDTO.builder().name("Category").build();

        String json = new ObjectMapper().writeValueAsString(dto);

        given(service.create(Mockito.any(CategoryDTO.class)))
                .willThrow(new BusinessException("Nome já cadastrado."));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CATEGORY_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform( request )
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath("errorList", hasSize(1)))
                .andExpect( jsonPath("message").value("Nome já cadastrado."));

    }
}
