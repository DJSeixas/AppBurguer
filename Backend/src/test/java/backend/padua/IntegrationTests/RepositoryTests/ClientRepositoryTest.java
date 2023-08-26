package backend.padua.IntegrationTests.RepositoryTests;


import backend.padua.model.Address;
import backend.padua.model.Client;
import backend.padua.model.Client;
import backend.padua.repositories.ClientRepository;
import backend.padua.repositories.ClientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class ClientRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ClientRepository repository;

    @Test
    @DisplayName("Deve obter um cliente por id.")
    public void findByIdTest() {

        Address ad = Address.builder().id(1L).address("Rua").neighborhood("Bairro").number(10)
                .complement("Complement").city("Cidade").state("Estado").cep("000000000").build();


        List<Address> adList = new ArrayList<>();
        adList.add(ad);

        Set<String> phone = new HashSet<>();
        phone.add("00123456789");


        Client cli = Client.builder().name("Client").email("x@xx.com")
                .cpf("123456789").addresses(adList).telephones(phone).build();

        entityManager.persist(cli);
        entityManager.merge(cli);

        var foundCli = repository.findById(cli.getId());

        assertThat( foundCli.isPresent() ).isTrue();
    }

    @Test
    @DisplayName("Deve salvar um cliente.")
    public void saveClientTest() {
        Client cli = Client.builder().name("Client").build();

        Client savedCli = repository.save(cli);

        assertThat( savedCli.getId() ).isNotNull();
    }

    @Test
    @DisplayName("Deve salvar uma client.")
    public void deleteClientTest() {

        Client cli = Client.builder().name("Client").build();
        entityManager.persist(cli);

        Client foundCli = entityManager.find(Client.class, cli.getId());

        repository.delete(foundCli);

        Client deleteCat = entityManager.find(Client.class, cli.getId());

        assertThat(deleteCat).isNull();
    }

}
