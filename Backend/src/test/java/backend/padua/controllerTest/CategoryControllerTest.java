package backend.padua.controllerTest;

import backend.padua.controllers.CategoryController;
import backend.padua.data.dto.CategoryDTO;
import backend.padua.exceptions.BusinessException;
import backend.padua.services.CategoryService;
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
    public void createCategoryWithDuplicateNameTest() throws Exception{

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

    @Test
    @DisplayName("Deve atualizar uma categoria.")
    public void updateCategoryTest() throws Exception{

        Long id = 1L;

        CategoryDTO dto = CategoryDTO.builder().id(id).name("Category").build();

        String json = new ObjectMapper().writeValueAsString(dto);

        given(service.findById(Mockito.anyLong()))
                .willReturn(dto);

        CategoryDTO updatedCat = CategoryDTO.builder().id(id).name("CategoryUpdated").build();

        given(service.update(dto))
                .willReturn(updatedCat);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(CATEGORY_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform( request )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("id").value(id) )
                .andExpect( jsonPath("name").value(updatedCat.getName()) );
    }

    @Test
    @DisplayName("Deve retornar uma lista das categorias.")
    public void findAllCategoriesTest() throws Exception {

        CategoryDTO category1 = CategoryDTO.builder().id(1L).name("Category1").build();
        CategoryDTO category2 = CategoryDTO.builder().id(2L).name("Category2").build();
        CategoryDTO category3 = CategoryDTO.builder().id(3L).name("Category3").build();

        List<CategoryDTO> categories = new ArrayList<>();

        categories.add(category1);
        categories.add(category2);
        categories.add(category3);

        given(service.findAll()).willReturn(categories);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CATEGORY_API)
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform( request )
                .andExpect( status().isOk() )
                .andExpect( content().contentType(MediaType.APPLICATION_JSON) )
                .andExpect( jsonPath("$", hasSize(3)) )

                .andExpect( jsonPath("$[0].id").value(category1.getId()) )
                .andExpect( jsonPath("$[0].name").value(category1.getName()) )

                .andExpect( jsonPath("$[1].id").value(category2.getId()) )
                .andExpect( jsonPath("$[1].name").value(category2.getName()) )

                .andExpect( jsonPath("$[2].id").value(category3.getId()) )
                .andExpect( jsonPath("$[2].name").value(category3.getName()) );

    }

    @Test
    @DisplayName("Deve obter informações de uma categoria.")
    public void getCategoryByIdTest() throws Exception {

        Long id = 1L;

        CategoryDTO category = CategoryDTO.builder().id(1L).name("Category").build();

        given(service.findById(id)).willReturn(category);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CATEGORY_API.concat("/"+ 1))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform( request )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("id").value(id) )
                .andExpect( jsonPath("name").value(category.getName()) );
    }

    @Test
    @DisplayName("Deve deletar uma categoria.")
    public void deleteCategoryTest() throws Exception{

        given(service.findById(Mockito.anyLong())).willReturn(CategoryDTO.builder().id(1L).name("Category").build());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(CATEGORY_API.concat("/"+ 1));

        mvc
                .perform( request )
                .andExpect( status().isNoContent() );
    }

    @Test
    @DisplayName("Deve filtar categoria pelo nome.")
    public void findCategoryByNameTest() throws Exception{

        Long id = 1L;

        String name = "Category";

        CategoryDTO category = CategoryDTO.builder().id(1L).name(name).build();

        given(service.findByName(name)).willReturn(category);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CATEGORY_API.concat("/name/"+ name))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform( request )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("id").value(id) )
                .andExpect( jsonPath("name").value(name) );
    }
}
