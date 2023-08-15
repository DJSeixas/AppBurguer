package backend.padua.servicesTest;

import backend.padua.data.dto.CategoryDTO;
import backend.padua.data.dto.ProductDTO;
import backend.padua.model.Category;
import backend.padua.model.Product;
import backend.padua.repositories.ProductRepository;
import backend.padua.services.CategoryService;
import backend.padua.services.ProductService;
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
public class ProductServiceTest {

    ProductService service;

    @MockBean
    CategoryService catService;

    @MockBean
    ProductRepository repository;

    @Spy
    ModelMapper modelMapper;

    @BeforeEach
    public void setUp(){
        this.service = new ProductService(repository, catService, modelMapper);
    }

    @Test
    @DisplayName("Deve salvar um produto.")
    public void createProductTest() {

        ProductDTO dto = ProductDTO.builder().name("Product").price(10.0).category("Category").build();

        CategoryDTO catDto = CategoryDTO.builder().id(1L).name("Category").build();

        Category cat = Category.builder().id(1L).name("Category").build();

        Product entity = Product.builder().name("Product").price(10.0).category(cat).build();

        Product persisted = entity;
        persisted.setId(1L);

        dto.setId(1L);

        when(catService.findByName(catDto.getName())).thenReturn(catDto);
        when(repository.save(entity)).thenReturn(persisted);

        ProductDTO result = service.create(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getLinks()).isNotNull();

        assertThat(1L).isEqualTo(result.getId());
        assertThat("Product").isEqualTo(result.getName());
        assertThat(10.0).isEqualTo(result.getPrice());
        assertThat("Category").isEqualTo(result.getCategory());
        assertThat( result.toString().contains("links: [</api/products/1>;rel=\"self\"]") ).isTrue();

    }

    @Test
    @DisplayName("Deve lançar erro ao tentar criar produto nulo.")
    public void createInvalidProduct(){

        Throwable exception = catchThrowable(() ->
                service.create(null));

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    @DisplayName("Deve atualizar um produto.")
    public void updateProductTest() throws Exception{

        Long id = 1L;

        ProductDTO dto = ProductDTO.builder().name("newProduct").price(20.0).category("Category").build();

        Category cat = Category.builder().id(id).name("Category").build();

        Product entity = Product.builder().name("Product").price(10.0).category(cat).build();

        Product persisted = entity;
        persisted.setId(id);

        dto.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(persisted);

        ProductDTO result = service.update(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getLinks()).isNotNull();

        assertThat("newProduct").isEqualTo(result.getName());
        assertThat(20.0).isEqualTo(result.getPrice());
        assertThat("Category").isEqualTo(result.getCategory());
        assertThat( result.toString().contains("links: [</api/products/1>;rel=\"self\"]") ).isTrue();
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar atualizar produto nulo.")
    public void updateWithNullProductTest() throws Exception{

        Throwable exception = catchThrowable(() ->
                service.update(null));

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    @DisplayName("Deve obter uma lista dos produtos.")
    public void FindAllProductsTest(){

        ProductDTO dtoProduct1 = ProductDTO.builder().id(1L).name("Product1").build();
        ProductDTO dtoProduct2 = ProductDTO.builder().id(2L).name("Product2").build();
        ProductDTO dtoProduct3 = ProductDTO.builder().id(3L).name("Product3").build();

        List<ProductDTO> dtoProducts = new ArrayList<>();

        dtoProducts.add(dtoProduct1);
        dtoProducts.add(dtoProduct2);
        dtoProducts.add(dtoProduct3);

        Product product1 = Product.builder().id(1L).name("Product1").build();
        Product product2 = Product.builder().id(2L).name("Product2").build();
        Product product3 = Product.builder().id(3L).name("Product3").build();

        List<Product> products = new ArrayList<>();

        products.add(product1);
        products.add(product2);
        products.add(product3);

        when(repository.findAll()).thenReturn(products);

        List<ProductDTO> foundProducts = service.findAll();

        ProductDTO prod1 = foundProducts.get(0);
        ProductDTO prod2 = foundProducts.get(1);
        ProductDTO prod3 = foundProducts.get(2);

        assertThat( prod1 ).isNotNull();
        assertThat( prod1.getLinks() ).isNotNull();

        assertThat( prod1.getId() ).isEqualTo(1L);
        assertThat( "Product1" ).isEqualTo(prod1.getName());
        assertThat( prod1.toString().contains("links: [</api/products/1>;rel=\"self\"]") ).isTrue();

        assertThat( prod2 ).isNotNull();
        assertThat( prod2.getLinks() ).isNotNull();

        assertThat( prod2.getId() ).isEqualTo(2L);
        assertThat( "Product2" ).isEqualTo(prod2.getName());
        assertThat( prod2.toString().contains("links: [</api/products/2>;rel=\"self\"]") ).isTrue();

        assertThat( prod3 ).isNotNull();
        assertThat( prod3.getLinks() ).isNotNull();

        assertThat( prod3.getId() ).isEqualTo(3L);
        assertThat( "Product3" ).isEqualTo(prod3.getName());
        assertThat( prod3.toString().contains("links: [</api/products/3>;rel=\"self\"]") ).isTrue();
    }

    @Test
    @DisplayName("Deve lançar erro ao buscar uma lista de produtos vazia.")
    public void emptyProductsFindAllTest() {

        Throwable exception = catchThrowable(() ->
                service.findAll());

        String expectedMessage = "No products found!";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    @DisplayName("Deve encontrar um produto por Id.")
    public void findProductByIdTest(){

        Long id = 1L;

        Category cat = Category.builder().id(id).name("Category").build();

        Product prod = Product.builder().id(id).name("Product").price(10.0).category(cat).build();

        when(repository.findById(id)).thenReturn(Optional.of(prod));

        ProductDTO foundProd = service.findById(id);

        assertThat( foundProd ).isNotNull();
        assertThat( foundProd.getLinks() ).isNotNull();

        assertThat( foundProd.getId() ).isEqualTo(id);
        assertThat( foundProd.getName() ).isEqualTo(prod.getName());
        assertThat( foundProd.getPrice() ).isEqualTo(prod.getPrice());
        assertThat( foundProd.getCategory() ).isEqualTo(prod.getCategory().getName());
        assertThat( foundProd.toString().contains("links: [</api/products/1>;rel=\"self\"]") ).isTrue();
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar buscar produto nulo por Id.")
    public void categoryNotFoundByIdTest(){

        Throwable exception = catchThrowable(() ->
                service.findById(null));

        String expectedMessage = "No records found for this id!";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    @DisplayName("Deve deletar um produto.")
    public void deleteProductTest() throws Exception{

        Long id = 1L;

        Category cat = Category.builder().id(id).name("Category").build();

        Product entity = Product.builder().name("Product").price(10.0).category(cat).build();

        entity.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        service.delete(id);

        Mockito.verify(repository, Mockito.times(1)).delete(entity);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar deletar produto nulo.")
    public void deleteNullProductTest() throws Exception{

        Throwable exception = catchThrowable(() ->
                service.delete(null));

        String expectedMessage = "No records found for this id!";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).contains(expectedMessage);
    }
}
