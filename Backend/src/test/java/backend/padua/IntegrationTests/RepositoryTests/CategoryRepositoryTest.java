package backend.padua.IntegrationTests.RepositoryTests;


import backend.padua.model.Category;
import backend.padua.repositories.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CategoryRepository repository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir uma client na base com o nome informado")
    public void returnTrueWhenNameExists(){

        String name = "Category";

        Category cat = Category.builder().name("Category").build();

        entityManager.persist(cat);

        boolean exists = repository.existsByName(name);

        assertThat(exists).isTrue();

    }

    @Test
    @DisplayName("Deve retornar falso quando não existir uma client na base com o nome informado")
    public void returnFalseWhenNameExists(){

        String name = "Category";

        boolean exists = repository.existsByName(name);

        assertThat(exists).isFalse();

    }

    @Test
    @DisplayName("Deve obter uma client por id.")
    public void findByIdTest() {

        Category cat = Category.builder().name("Category").build();
        entityManager.persist(cat);

        var foundCat = repository.findById(cat.getId());

        assertThat( foundCat.isPresent() ).isTrue();
    }

    @Test
    @DisplayName("Deve obter uma client por nome.")
    public void findByNameTest() {

        Category cat = Category.builder().name("Category").build();
        entityManager.persist(cat);

        var foundCat = repository.findByName(cat.getName());

        assertThat( foundCat.isPresent() ).isTrue();
    }

    @Test
    @DisplayName("Deve salvar uma client.")
    public void saveCategoryTest() {
        Category cat = Category.builder().name("Category").build();

        Category savedCat = repository.save(cat);

        assertThat( savedCat.getId() ).isNotNull();
    }

    @Test
    @DisplayName("Deve salvar uma client.")
    public void deleteCategoryTest() {

        Category cat = Category.builder().name("Category").build();
        entityManager.persist(cat);

        Category foundCat = entityManager.find(Category.class, cat.getId());

        repository.delete(foundCat);

        Category deleteCat = entityManager.find(Category.class, cat.getId());

        assertThat(deleteCat).isNull();
    }

}
