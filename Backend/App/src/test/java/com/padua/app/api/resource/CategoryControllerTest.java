package com.padua.app.api.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.padua.app.controllers.CategoryController;
import com.padua.app.data.dto.CategoryDTO;
import com.padua.app.exceptions.BusinessExceptions;
import com.padua.app.model.entity.Category;
import com.padua.app.services.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
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

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = CategoryController.class)
@AutoConfigureMockMvc
public class CategoryControllerTest {

    static String Category_API = "/api/category";

    @Autowired
    MockMvc mvc;

    @MockBean
    CategoryService service;
    @Test
    @DisplayName("Deve criar uma categoria com sucesso.")
    public void createCategoryTest() throws Exception {

        CategoryDTO dto = createNewCategory();
        Category savedCategory = Category.builder().id(1L).name("Category").build();

        BDDMockito.given(service.create(any(Category.class))).willReturn(savedCategory);

        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(Category_API)
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
    @DisplayName("Deve lançar erro de validação quando não houver dados suficientres para a criação da categoria.")
    public void createInvalidCategoryTest() throws Exception {

        String json = new ObjectMapper().writeValueAsString(new CategoryDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(Category_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errorList", hasSize(1)));
    }

    @Test
    @DisplayName("Deve lançar erro ao ao tentar cadastar categoria com mesmo nome")
    public void createCategoryWithDuplicateNames() throws Exception {

        CategoryDTO dto = createNewCategory();

        String json = new ObjectMapper().writeValueAsString(dto);

        String errorMessage = "Nome já cadastrado.";

        BDDMockito.given(service.create(any(Category.class)))
                .willThrow(new BusinessExceptions(errorMessage));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(Category_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform( request )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errorList", hasSize(1)))
                .andExpect(jsonPath("errorList[0]").value(errorMessage));
    }

    @Test
    @DisplayName("Deve obter informações de uma categoria.")
    public void getCategoryDetailstest() throws Exception {

        Long id = 1L;

        Category category = Category.builder().id(id).name("category").build();

        BDDMockito.given(service.findById(id)).willReturn(Optional.of(category));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(Category_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform( request )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("name").value(category.getName()));
    }

    @Test
    @DisplayName("Deve obter resource not found quando a categoria procurada não existir..")
    public void categoryNotFoundTest() throws Exception {

        BDDMockito.given(service.findById(anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(Category_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform( request )
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Deve deletar uma categoria.")
    public void deleteCategoryTest() throws Exception {

        BDDMockito.given(service.findById(anyLong())).willReturn(Optional.of(Category.builder().id(1L).build()));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(Category_API.concat("/" + 1));

        mvc
                .perform( request )
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar resource nor found quando não encontrar a categoria para deletar..")
    public void deleteInexistentCategoryTest() throws Exception {

        BDDMockito.given(service.findById(anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(Category_API.concat("/" + 1));

        mvc
                .perform( request )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve atualizar uma categoria.")
    public void updateCategoryTest() throws Exception {

        Long id = 1L;
        String json = new ObjectMapper().writeValueAsString(createNewCategory());

        Category updatingcat = Category.builder().id(1L).name("some name").build();

        BDDMockito.given(service.findById(id))
                .willReturn(Optional.of(updatingcat));

        Category updatedcat = Category.builder().id(id).name("Category").build();

        BDDMockito.given(service.update(updatingcat)).willReturn(updatedcat);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put( Category_API.concat("/" + 1))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mvc
                .perform( request )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("id").value(id) )
                .andExpect( jsonPath("name").value(createNewCategory().getName()) );
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar atualizar Categoria inexistente.")
    public void updateInexistentCategoryTest() throws Exception {

        String json = new ObjectMapper().writeValueAsString(createNewCategory());

        BDDMockito.given(service.findById(anyLong()))
                .willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put( Category_API.concat("/") + 1 )
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mvc
                .perform( request )
                .andExpect( status().isNotFound() );
    }

    private CategoryDTO createNewCategory() {

        return CategoryDTO.builder().name("Category").build();
    }
}
