package br.com.rochasoft.libraryapi.service;

import br.com.rochasoft.libraryapi.exception.BusinessException;
import br.com.rochasoft.libraryapi.model.entity.Book;
import br.com.rochasoft.libraryapi.model.repository.BookRepository;
import br.com.rochasoft.libraryapi.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest
{

    BookService     service;

    @MockBean
    BookRepository  repository;

    @BeforeEach
    public void setup()
    {
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest()
    {

        // cenário
        Book book = createValidBook();

        // coloca uma regra para este cenário
        // retorna 'false' ao executar a função 'repository.existsByIsbn'
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);

        // simula a utilização do método 'save' do 'repository'
        Mockito.when(repository.save(book)).thenReturn(Book.builder().id(11).isbn("123").title("As Aventuras").author("Fulano").build());

        // execução
        Book savedBook = service.save(book);

        // verificação
        Assertions.assertThat(savedBook.getId()).isNotNull();
        Assertions.assertThat(savedBook.getIsbn()).isEqualTo("123");
        Assertions.assertThat(savedBook.getTitle()).isEqualTo("As Aventuras");
        Assertions.assertThat(savedBook.getAuthor()).isEqualTo("Fulano");

    }

    private Book createValidBook()
    {
        return Book.builder().isbn("123").author("Fulano").title("As Aventuras").build();
    }

    @Test
    @DisplayName("Deve gerar erro ao tentar registrar um livro com ISBN duplicado")
    public void shouldNotSaveABookWithDuplicatedISBN()
    {

        // cenário
        Book book = createValidBook();

        // coloca uma regra para este cenário
        // retorna 'true' ao executar a função 'repository.existsByIsbn'
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        // execução
        Throwable exception = Assertions.catchThrowable( () -> service.save(book));

        // verifica se o erro foi gerado (isbn já cadastrado)
        Assertions.assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn já cadastrado");

        // verifica se nunca irá executar o método salvar com o parametro 'book'
        Mockito.verify(repository, Mockito.never()).save(book);


    }

}
