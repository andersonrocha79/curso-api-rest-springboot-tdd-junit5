package br.com.rochasoft.libraryapi.model.repository;

import br.com.rochasoft.libraryapi.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long>
{

    boolean existsByIsbn(String isbn);

}
