package com.padua.app.api.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.padua.app.data.dto.CategoryDTO;
import com.padua.app.exceptions.BusinessExceptions;
import com.padua.app.model.entity.Category;
import com.padua.app.services.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
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

        BDDMockito.given(service.create(Mockito.any(Category.class))).willReturn(savedCategory);

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

        BDDMockito.given(service.create(Mockito.any(Category.class)))
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

    private CategoryDTO createNewCategory() {
        return CategoryDTO.builder().name("Category").build();
    }
}
