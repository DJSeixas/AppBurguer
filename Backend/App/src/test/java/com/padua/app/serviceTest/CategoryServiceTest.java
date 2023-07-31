package com.padua.app.serviceTest;

import com.padua.app.exceptions.BusinessExceptions;
import com.padua.app.model.entity.Category;
import com.padua.app.repositories.CategoryRepository;
import com.padua.app.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CategoryServiceTest {

    CategoryService service;

    @MockBean
    CategoryRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new CategoryService(repository);
    }

    @Test
    @DisplayName("Deve salvar uma categoria.")
    public void createCategoryTest() {

        Category category = createValidCategory();

        Mockito.when(repository.existsByName(Mockito.anyString())).thenReturn(false);

        Mockito.when( repository.save(category) ).thenReturn(Category.builder()
                .id(1L)
                .name("Category")
                .build()
        );

        Category savedCategory = service.create(category);

        assertThat(savedCategory.getId()).isNotNull();
        assertThat(savedCategory.getName()).isEqualTo("Category");
    }

    @Test
    @DisplayName("Deve lançar erro de negócio ao tentar salvar uma categoria com nome duplicado")
    public void shouldNotSaveACategoryWithDuplicateName() {
        Category category = createValidCategory();

        Mockito.when(repository.existsByName(Mockito.anyString())).thenReturn(true);

        Throwable exception = catchThrowable(() -> service.create(category));

        assertThat(exception)
                .isInstanceOf(BusinessExceptions.class)
                .hasMessage("Nome já cadastrado.");

        Mockito.verify(repository, Mockito.never()).save(category);
    }

    private Category createValidCategory(){
        return Category.builder().name("Category").build();
    }
}
