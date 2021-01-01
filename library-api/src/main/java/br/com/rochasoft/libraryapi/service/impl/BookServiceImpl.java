package br.com.rochasoft.libraryapi.service.impl;

import br.com.rochasoft.libraryapi.exception.BusinessException;
import br.com.rochasoft.libraryapi.model.entity.Book;
import br.com.rochasoft.libraryapi.model.repository.BookRepository;
import br.com.rochasoft.libraryapi.service.BookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService
{

    private BookRepository repository;

    public BookServiceImpl(BookRepository repository)
    {
        this.repository = repository;
    }

    @Override
    public Book save(Book book)
    {

        // verifica se o isbn já está cadastrado
        if (repository.existsByIsbn(book.getIsbn()))
        {
            throw new BusinessException("Isbn já cadastrado");
        }

        return repository.save(book);

    }

}
