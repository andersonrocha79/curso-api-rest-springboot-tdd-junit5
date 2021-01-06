package br.com.rochasoft.libraryapi.model.repository;

import br.com.rochasoft.libraryapi.model.entity.Book;
import br.com.rochasoft.libraryapi.model.entity.Loan;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static br.com.rochasoft.libraryapi.model.repository.BookRepositoryTest.createNewBook;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest    // indica que vai fazer teste de integração com banco em memória
public class LoanRepositoryTest
{

    @Autowired
    private LoanRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Deve verificar se existe empréstimo não devolvido para o livro")
    public void existsByBookAndNotReturnedTest()
    {

        // cenário

        // cria um livro
        Loan loan = createAndPersistLoan();
        Book book = loan.getBook();

        // execução
        boolean exists = repository.existsByBookAndNotReturned(book);

        // verificação
        Assertions.assertThat(exists).isTrue();

    }

    @Test
    @DisplayName("Deve buscar empréstimo pelo isbn do livro ou customer")
    public void findByBookIsbnOrCustomerTest()
    {

        // cenário
        // cria um livro e um empréstimo com este livro
        Loan loan = createAndPersistLoan();

        // seleciona o único registro que foi incluído
        Page<Loan> result = repository.findByBookIsbnOrCustomer("123", "Fulano", PageRequest.of(0, 10));

        // verificaçõs
        Assertions.assertThat(result.getContent()).hasSize(1);
        Assertions.assertThat(result.getContent()).contains(loan);
        Assertions.assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        Assertions.assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);

    }

    public Loan createAndPersistLoan()
    {

        // cria um livro
        Book book = createNewBook("123");
        entityManager.persist(book);

        // registra o empréstimo do livro
        Loan loan = Loan.builder().book(book).customer("Fulano").loanDate(LocalDate.now()).build();
        entityManager.persist(loan);

        return loan;

    }

}
