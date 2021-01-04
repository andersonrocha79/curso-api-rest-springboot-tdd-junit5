package br.com.rochasoft.libraryapi.api.resource;

import br.com.rochasoft.libraryapi.api.dto.BookDTO;
import br.com.rochasoft.libraryapi.api.exception.ApiErros;
import br.com.rochasoft.libraryapi.exception.BusinessException;
import br.com.rochasoft.libraryapi.model.entity.Book;
import br.com.rochasoft.libraryapi.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController
{

    private BookService service;
    private ModelMapper modelMapper;

    public BookController(BookService service, ModelMapper mapper)
    {
        this.service     = service;
        this.modelMapper = mapper;
    }

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

}
