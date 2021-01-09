package br.com.rochasoft.libraryapi.api.resource;

import br.com.rochasoft.libraryapi.api.dto.BookDTO;
import br.com.rochasoft.libraryapi.api.dto.LoanDTO;
import br.com.rochasoft.libraryapi.model.entity.Book;
import br.com.rochasoft.libraryapi.model.entity.Loan;
import br.com.rochasoft.libraryapi.service.BookService;
import br.com.rochasoft.libraryapi.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController
{

    private final BookService service;
    private final ModelMapper modelMapper;
    private final LoanService loanService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO dto)
    {

        final Book entity = modelMapper.map(dto, Book.class);

        service.save(entity);

        return modelMapper.map(entity, BookDTO.class);

    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDTO get(@PathVariable long id)
    {
        return service
                .getById(id)
                .map(book -> modelMapper.map(book, BookDTO.class))
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id)
    {
        Book book = service
                .getById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        service.delete(book);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDTO update( @PathVariable long id, BookDTO dto)
    {

        // busca o livro
        return service
                .getById(id)
                .map(book ->
                {
                    // atualiza os campos do objeto
                    book.setAuthor(dto.getAuthor());
                    book.setTitle(dto.getTitle());

                    // envia a atualização para o banco de dados
                    book = service.update(book);

                    // retorna o objeto 'atualizado'
                    return modelMapper.map(book, BookDTO.class);

                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    @GetMapping
    public Page<BookDTO> find(BookDTO dto, Pageable pageRequest)
    {

        Book filter = modelMapper.map(dto, Book.class);
        Page<Book> result = service.find(filter, pageRequest);

        List<BookDTO> list =  result.getContent()
                                    .stream() // gera um 'stream'
                                    .map(entity -> modelMapper.map(entity, BookDTO.class)) // faz a conversão para 'bookDTO'
                                    .collect(Collectors.toList()); // pega o resultado e gera uma coleção 'list'

        return new PageImpl<BookDTO> (list, pageRequest, result.getTotalElements());

    }

    // sub recurso de livros (empréstimos do livro passado como parâmetro
    @GetMapping("{id}/loans")
    public Page<LoanDTO> loansByBook(@PathVariable Long id, Pageable pageable)
    {

        Book book = service.getById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Page<Loan> result = loanService.getLoansByBook(book, pageable);

        List<LoanDTO> list =  result.getContent()
                             .stream() // gera um 'stream'
                             .map( loan ->
                             {

                                 Book loanBook   = loan.getBook();
                                 BookDTO bookDTO = modelMapper.map(loanBook, BookDTO.class);
                                 LoanDTO loanDTO = modelMapper.map(loan, LoanDTO.class);
                                 loanDTO.setBook(bookDTO);

                                 return loanDTO;

                             }) // faz a conversão para 'loanDTO'
                             .collect(Collectors.toList()); // pega o resultado e gera uma coleção 'list'

        return new PageImpl<LoanDTO> (list, pageable, result.getTotalElements());

    }

}
