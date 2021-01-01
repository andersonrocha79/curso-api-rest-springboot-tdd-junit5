package br.com.rochasoft.libraryapi.api.resource;

import br.com.rochasoft.libraryapi.api.dto.BookDTO;
import br.com.rochasoft.libraryapi.exception.BusinessException;
import br.com.rochasoft.libraryapi.model.entity.Book;
import br.com.rochasoft.libraryapi.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest
{

    static String BOOK_API = "/api/books";

    @Autowired
    MockMvc mvc;

    @MockBean
    BookService service;

    @Test
    @DisplayName("Deve criar um livro com sucesso.")
    public void createBookTest() throws Exception
    {

        BookDTO dto = createBook();

        Book savedBook = Book.builder()
                             .id(100)
                             .author("Arthur")
                             .title("As aventuras")
                             .isbn("001")
                             .build();

        BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(savedBook);

        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                                                .post(BOOK_API)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .accept(MediaType.APPLICATION_JSON)
                                                .content(json);

        mvc.perform(request)
           .andExpect(status().isCreated())
           .andExpect(jsonPath("id").value(dto.getId()))
           .andExpect(jsonPath("title").value(dto.getTitle()))
           .andExpect(jsonPath("author").value(dto.getAuthor()))
           .andExpect(jsonPath("isbn").value(dto.getIsbn()));

    }

    private BookDTO createBook()
    {
        return BookDTO.builder()
                .author("Arthur")
                .title("As aventuras")
                .isbn("001")
                .build();
    }

    @Test
    @DisplayName("Deve gerar erro ao tentar criar um livro com dados incompletos.")
    public void createInvalidBookTest() throws Exception
    {

        String json = new ObjectMapper().writeValueAsString(new BookDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
           .andExpect( status().isBadRequest())
           .andExpect( jsonPath("erros", Matchers.hasSize(3)));
    }

    @Test
    @DisplayName("Deve gerar erro ao tentar criar um livro com ISBN já utilizado")
    public void createBookWithDuplicatedIsbn() throws Exception
    {

        BookDTO dto = createBook();

        String json = new ObjectMapper().writeValueAsString(dto);

        String mensagemErro = "Isbn já cadastrado";

        // simulação de geração do erro 'isbn já cadastrado' quando o 'service.save' for executado
        BDDMockito.given(service.save(Mockito.any(Book.class))).willThrow(new BusinessException(mensagemErro));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect( status().isBadRequest())
                .andExpect( jsonPath("erros", Matchers.hasSize(1)))
                .andExpect( jsonPath("erros[0]").value(mensagemErro));
    }

}
