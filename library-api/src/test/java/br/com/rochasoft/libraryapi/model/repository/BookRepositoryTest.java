package br.com.rochasoft.libraryapi.model.repository;

import br.com.rochasoft.libraryapi.model.entity.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest                            // indica que irá fazer testes com 'jpa' (cria banco dados em memória para executar os testes) (h2 database)
public class BookRepositoryTest
{

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository repository;

    @Test
    @DisplayName("Deve retornar true porque o isbn já existe na base")
    public void returnTrueWhenIsbnExists()
    {

        // cenário
        String isbn = "123";

        // grava um livro
        Book book = Book.builder().title("Aventuras").author("Fulano").isbn(isbn).build();
        entityManager.persist(book);

        // execução
        boolean exists = repository.existsByIsbn(isbn);

        // verificação (tem que retornar verdadeiro)
        Assertions.assertThat(exists).isTrue();

    }

    @Test
    @DisplayName("Deve retornar falso porque o isbn não existe na base")
    public void returnFalseWhenIsbnDoesntExists()
    {

        // cenário
        String isbn = "123";

        // execução
        boolean exists = repository.existsByIsbn(isbn);

        // verificação (tem que retornar falso)
        Assertions.assertThat(exists).isFalse();

    }

}
