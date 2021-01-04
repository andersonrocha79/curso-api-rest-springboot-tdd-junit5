package br.com.rochasoft.libraryapi.api.resource;

import br.com.rochasoft.libraryapi.api.dto.LoanDTO;
import br.com.rochasoft.libraryapi.model.entity.Book;
import br.com.rochasoft.libraryapi.model.entity.Loan;
import br.com.rochasoft.libraryapi.service.BookService;
import br.com.rochasoft.libraryapi.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController
{

    private final LoanService service;
    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public long create(@RequestBody LoanDTO dto)
    {

        Book book = bookService
                .getBookByIsbn(dto.getIsbn())
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found for passed isbn"));

        Loan entity = Loan.builder()
                .book(book)
                .customer(dto.getCustomer())
                .loanDate(LocalDate.now())
                .build();

        entity = service.save(entity);

        return entity.getId();

    }

}
