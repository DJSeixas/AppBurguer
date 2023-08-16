package backend.padua.servicesTest;

import backend.padua.data.dto.CategoryDTO;
import backend.padua.exceptions.BusinessException;
import backend.padua.model.Category;
import backend.padua.repositories.CategoryRepository;
import backend.padua.services.CategoryService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
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
        assertThat(result.getLinks()).isNotNull();

        assertThat("Category").isEqualTo(result.getName());
        assertThat( result.toString().contains("links: [</api/categories/1>;rel=\"self\"]") ).isTrue();
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
    public void shouldNotCreateWithNullCategoryTest(){

        Throwable exception = catchThrowable(() ->
                service.create(null));

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    @DisplayName("Deve obter uma lista das categorias.")
    public void FindAllTest(){

        CategoryDTO dtoCategory1 = CategoryDTO.builder().id(1L).name("Category1").build();
        CategoryDTO dtoCategory2 = CategoryDTO.builder().id(2L).name("Category2").build();
        CategoryDTO dtoCategory3 = CategoryDTO.builder().id(3L).name("Category3").build();

        List<CategoryDTO> dtoCategories = new ArrayList<>();

        dtoCategories.add(dtoCategory1);
        dtoCategories.add(dtoCategory2);
        dtoCategories.add(dtoCategory3);

        Category category1 = Category.builder().id(1L).name("Category1").build();
        Category category2 = Category.builder().id(2L).name("Category2").build();
        Category category3 = Category.builder().id(3L).name("Category3").build();

        List<Category> categories = new ArrayList<>();

        categories.add(category1);
        categories.add(category2);
        categories.add(category3);

        when(repository.findAll()).thenReturn(categories);

        List<CategoryDTO> foundCategories = service.findAll();

        CategoryDTO cat1 = foundCategories.get(0);
        CategoryDTO cat2 = foundCategories.get(1);
        CategoryDTO cat3 = foundCategories.get(2);

        assertThat( cat1 ).isNotNull();
        assertThat( cat1.getLinks() ).isNotNull();

        assertThat( cat1.getId() ).isEqualTo(dtoCategory1.getId());
        assertThat( cat1.getName() ).isEqualTo(dtoCategory1.getName());
        assertThat( cat1.toString().contains("links: [</api/categories/1>;rel=\"self\"]") ).isTrue();

        assertThat( cat2 ).isNotNull();
        assertThat( cat2.getLinks() ).isNotNull();

        assertThat( cat2.getId() ).isEqualTo(dtoCategory2.getId());
        assertThat( cat2.getName() ).isEqualTo(dtoCategory2.getName());
        assertThat( cat2.toString().contains("links: [</api/categories/2>;rel=\"self\"]") ).isTrue();

        assertThat( cat3 ).isNotNull();
        assertThat( cat3.getLinks() ).isNotNull();

        assertThat( cat3.getId() ).isEqualTo(dtoCategory3.getId());
        assertThat( cat3.getName() ).isEqualTo(dtoCategory3.getName());
        assertThat( cat3.toString().contains("links: [</api/categories/3>;rel=\"self\"]") ).isTrue();
    }

    @Test
    @DisplayName("Deve lançar erro ao buscar uma lista de categorias vazia.")
    public void emptyCategoriesFindAllTest() {

        Throwable exception = catchThrowable(() ->
                service.findAll());

        String expectedMessage = "No categories found!";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    @DisplayName("Deve obter uma categoria por id")
    public void findByIdTest() {
        Long id = 1L;

        Category cat = Category.builder().id(id).name("Category").build();

        when(repository.findById(id)).thenReturn(Optional.of(cat));

        CategoryDTO foundCat = service.findById(id);

        assertThat( foundCat ).isNotNull();
        assertThat( foundCat.getLinks() ).isNotNull();

        assertThat( foundCat.getId() ).isEqualTo(id);
        assertThat( foundCat.getName() ).isEqualTo(cat.getName());
        assertThat( foundCat.toString().contains("links: [</api/categories/1>;rel=\"self\"]") ).isTrue();
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar buscar categoria nula por Id.")
    public void categoryNotFoundByIdTest(){

        Throwable exception = catchThrowable(() ->
                service.findById(null));

        String expectedMessage = "No records found for this id!";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    @DisplayName("Deve obter uma categoria por nome.")
    public void findByNameTest() {
        Long id = 1L;

        String name = "Category";

        Category cat = Category.builder().id(id).name(name).build();

        when(repository.findByName(name)).thenReturn(Optional.of(cat));

        CategoryDTO foundCat = service.findByName(name);

        assertThat( foundCat ).isNotNull();
        assertThat( foundCat.getLinks() ).isNotNull();

        assertThat( foundCat.getId() ).isEqualTo(id);
        assertThat( foundCat.getName() ).isEqualTo(cat.getName());
        assertThat( foundCat.toString().contains("links: [</api/categories/1>;rel=\"self\"]") ).isTrue();
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar buscar categoria nula por nome.")
    public void categoryNotFoundByNameTest(){

        Throwable exception = catchThrowable(() ->
                service.findByName(null));

        String expectedMessage = "No records found for this name!";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    @DisplayName("Deve atualizar uma categoria.")
    public void updateCategoryTest() throws Exception{

        Long id = 1L;

        CategoryDTO dto = CategoryDTO.builder().name("newCategory").build();

        Category entity = Category.builder().name("Category").build();

        Category persisted = entity;
        persisted.setId(id);

        dto.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(persisted);

        CategoryDTO result = service.update(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getLinks()).isNotNull();

        assertThat("newCategory").isEqualTo(result.getName());
        assertThat( result.toString().contains("links: [</api/categories/1>;rel=\"self\"]") ).isTrue();

    }

    @Test
    @DisplayName("Deve lançar erro ao tentar atualizar categoria nula.")
    public void updateWithNullCategoryTest() throws Exception{

        Throwable exception = catchThrowable(() ->
                service.update(null));

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    @DisplayName("Deve deletar uma categoria.")
    public void deleteCategoryTest() throws Exception{

        Long id = 1L;

        Category entity = Category.builder().name("Category").build();

        entity.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        service.delete(id);

        Mockito.verify(repository, Mockito.times(1)).delete(entity);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar deletar categoria nula.")
    public void deleteNullCategoryTest() throws Exception{

        Throwable exception = catchThrowable(() ->
                service.delete(null));

        String expectedMessage = "No records found for this id!";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).contains(expectedMessage);
    }
}
