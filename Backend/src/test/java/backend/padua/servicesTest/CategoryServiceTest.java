package backend.padua.servicesTest;

import backend.padua.data.dto.CategoryDTO;
import backend.padua.exceptions.BusinessException;
import backend.padua.exceptions.RequiredObjectIsNullException;
import backend.padua.model.Category;
import backend.padua.repositories.CategoryRepository;
import backend.padua.services.CategoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CategoryServiceTest {

    CategoryService service;

    @MockBean
    CategoryRepository repository;

    @Spy
    ModelMapper modelMapper;

    @BeforeEach
    public void setUp(){
        this.service = new CategoryService(repository, modelMapper);
    }

    @Test
    @DisplayName("Deve salvar uma categoria.")
    public void createCategoryTest() {

        CategoryDTO dto = CategoryDTO.builder().name("Category").build();

        Category entity = Category.builder().name("Category").build();

        Category persisted = entity;
        persisted.setId(1L);

        dto.setId(1L);

        when(repository.save(entity)).thenReturn(persisted);

        CategoryDTO result = service.create(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();

        assertThat("Category").isEqualTo(result.getName());

    }

    @Test
    @DisplayName("Deve lançar erro de negócio ao tentar salvar livro duplicado")
    public void shouldNotSaveCategoryWithDuplicateName() {

        CategoryDTO dto = CategoryDTO.builder().name("Category").build();

        Category entity = Category.builder().name("Category").build();

        when( repository.existsByName(Mockito.anyString()) ).thenReturn(true);

        Throwable exception = catchThrowable(() ->
            service.create(dto));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Nome já cadastrado.");

        Mockito.verify(repository, Mockito.never()).save(entity);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar criar categoria nula.")
    public void shouldNotCreateWithNullCategory(){

        Throwable exception = catchThrowable(() ->
                service.create(null));

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).contains(expectedMessage);
    }
}
