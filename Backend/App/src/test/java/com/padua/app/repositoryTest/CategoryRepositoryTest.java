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
}
