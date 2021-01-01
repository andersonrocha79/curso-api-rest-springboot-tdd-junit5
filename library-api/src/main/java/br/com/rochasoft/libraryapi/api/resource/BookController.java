package br.com.rochasoft.libraryapi.api.resource;

import br.com.rochasoft.libraryapi.api.dto.BookDTO;
import br.com.rochasoft.libraryapi.api.exception.ApiErros;
import br.com.rochasoft.libraryapi.exception.BusinessException;
import br.com.rochasoft.libraryapi.model.entity.Book;
import br.com.rochasoft.libraryapi.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    // este método será executado sempre que esta exception for gerada no 'create' (por causa da diretiva @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErros handleValidationExceptions(MethodArgumentNotValidException ex)
    {

        BindingResult bindingResult = ex.getBindingResult();

        return new ApiErros(bindingResult);

    }

    // este método será executado sempre que falhar validação de dados no create (por causa da diretiva @Valid)
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErros handleBusinessException(BusinessException ex)
    {
        return new ApiErros(ex);
    }

}
