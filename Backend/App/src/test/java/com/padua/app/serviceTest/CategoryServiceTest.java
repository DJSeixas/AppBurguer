package com.padua.app.serviceTest;

import com.padua.app.exceptions.BusinessExceptions;
import com.padua.app.model.entity.Category;
import com.padua.app.repositories.CategoryRepository;
import com.padua.app.services.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.swing.text.html.Option;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


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

        Category cat = createValidCategory();

        when(repository.existsByName(anyString())).thenReturn(false);

        when( repository.save(cat) ).thenReturn(Category.builder()
                .id(1L)
                .name("Category")
                .build()
        );

        Category savedCategory = service.create(cat);

        assertThat(savedCategory.getId()).isNotNull();
        assertThat(savedCategory.getName()).isEqualTo("Category");
    }

    @Test
    @DisplayName("Deve lançar erro de negócio ao tentar salvar uma categoria com nome duplicado")
    public void shouldNotSaveACategoryWithDuplicateName() {
        Category category = createValidCategory();

        when(repository.existsByName(anyString())).thenReturn(true);

        Throwable exception = catchThrowable(() -> service.create(category));

        assertThat(exception)
                .isInstanceOf(BusinessExceptions.class)
                .hasMessage("Nome já cadastrado.");

        verify(repository, never()).save(category);
    }

    @Test
    @DisplayName("Deve obter um livro por Id")
    public void findByIdTest() {
        Long id = 1L;

        Category cat = createValidCategory();
        cat.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(cat));

        Optional<Category> foundCat = service.findById(id);

        assertThat( foundCat.isPresent() ).isTrue();
        assertThat(foundCat.get().getId()).isEqualTo(id);
        assertThat(foundCat.get().getName()).isEqualTo(cat.getName());
    }
    @Test
    @DisplayName("Deve retornar vazio ao obter uma categoria por Id quando não existe na base.")
    public void cateoryNotFoundByIdTest() {
        Long id = 1L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<Category> cat = service.findById(id);

        assertThat( cat.isPresent() ).isFalse();
    }

    @Test
    @DisplayName("Deve deletar uma categoria.")
    public void deleteCategorytest() {

        Category cat = Category.builder().id(1L).build();

        assertDoesNotThrow(() -> service.delete(cat));

        verify(repository, times(1)).delete(cat);
    }

    @Test
    @DisplayName("Deve erro ao tentar deletar uma categoria inexistente.")
    public void deleteInvalidCategoryTest() {

        Category cat = new Category();

        assertThrows(IllegalArgumentException.class, () -> service.delete(cat));

        verify(repository, never() ).delete(cat);
    }

    @Test
    @DisplayName("Deve atualizar uma categoria.")
    public void updateCategoryTest() {

        Long id = 1L;

        Category updatingCat = Category.builder().id(id).build();

        Category updatedCat = createValidCategory();
        updatedCat.setId(id);

        when(repository.save(updatingCat)).thenReturn(updatedCat);

        Category cat = service.update(updatingCat);

        assertThat(cat.getId()).isEqualTo(updatedCat.getId());
        assertThat(cat.getName()).isEqualTo(updatedCat.getName());
    }

    @Test
    @DisplayName("Deve erro ao tentar atualizar uma categoria inexistente.")
    public void updateInvalidCategoryTest() {

        Category cat = new Category();

        assertThrows(IllegalArgumentException.class, () -> service.update(cat));

        verify(repository, never() ).delete(cat);
    }


    private Category createValidCategory(){
        return Category.builder().name("Category").build();
    }
}
