package com.padua.app.repositoryTest;

import com.padua.app.model.entity.Category;
import com.padua.app.repositories.CategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

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
    @DisplayName("Deve retornar verdadeiro quando existir uma categoria na base com o nome informado.")
    public void returnTrueWhenNameExists() {

        String name = "category";
        Category category = Category.builder().name(name).build();
        entityManager.persist(category);

        boolean exists = repository.existsByName(name);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir uma categoria na base com o nome informado.")
    public void returnFalseWhenNameDoesntExists() {

        String name = "category";

        boolean exists = repository.existsByName(name);

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve obter uma categoria por id.")
    public void findByIdTest(){

        String name = "category";
        Category category = Category.builder().name(name).build();
        entityManager.persist(category);

        Optional<Category> foundCat = repository.findById(category.getId());

        assertThat(foundCat.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Deve salvar uma categoria.")
    public void saveCategoryTest(){

        String name = "category";
        Category category = Category.builder().name(name).build();

        Category savedCategory = repository.save(category);

        assertThat(savedCategory.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve deletar uma categoria.")
    public void deleteCategoryTest(){

        String name = "category";
        Category category = Category.builder().name(name).build();
        entityManager.persist(category);

        Category foundCat = entityManager.find(Category.class, category.getId());

        repository.delete(foundCat);

        Category deletedCat = entityManager.find(Category.class, category.getId());

        assertThat(deletedCat).isNull();
    }
}
