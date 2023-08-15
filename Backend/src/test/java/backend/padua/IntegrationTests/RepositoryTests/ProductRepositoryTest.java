package backend.padua.IntegrationTests.RepositoryTests;

import backend.padua.model.Category;
import backend.padua.model.Product;
import backend.padua.repositories.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ProductRepository repository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um produto na base com o nome informado")
    public void returnTrueWhenNameExists(){

        Category cat = Category.builder().name("Category").build();

        entityManager.persist(cat);

        String name = "Product";

        Product prod = Product.builder().name("Product").price(10.0).category(cat).build();

        entityManager.persist(prod);

        boolean exists = repository.existsByName(name);

        assertThat(exists).isTrue();

    }

    @Test
    @DisplayName("Deve retornar falso quando não existir um produto na base com o nome informado")
    public void returnFalseWhenNameExists(){

        String name = "Product";

        boolean exists = repository.existsByName(name);

        assertThat(exists).isFalse();

    }

    @Test
    @DisplayName("Deve obter um produto por id.")
    public void findByIdTest() {

        Category cat = Category.builder().name("Category").build();

        entityManager.persist(cat);

        Product prod = Product.builder().name("Product").price(10.0).category(cat).build();
        entityManager.persist(prod);

        var foundProd = repository.findById(prod.getId());

        assertThat( foundProd.isPresent() ).isTrue();
    }

    @Test
    @DisplayName("Deve salvar um produto.")
    public void saveProductTest() {

        Category cat = Category.builder().name("Category").build();

        entityManager.persist(cat);

        Product prod = Product.builder().name("Product").price(10.0).category(cat).build();

        Product savedCat = repository.save(prod);

        assertThat( savedCat.getId() ).isNotNull();
    }

    @Test
    @DisplayName("Deve salvar um produto.")
    public void deleteProductTest() {

        Category cat = Category.builder().name("Category").build();

        entityManager.persist(cat);

        Product prod = Product.builder().name("Product").price(10.0).category(cat).build();
        entityManager.persist(prod);

        Product foundProd = entityManager.find(Product.class, prod.getId());

        repository.delete(foundProd);

        Product deleteCat = entityManager.find(Product.class, prod.getId());

        assertThat(deleteCat).isNull();
    }
}
