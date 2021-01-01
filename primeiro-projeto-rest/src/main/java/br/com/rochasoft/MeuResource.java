package br.com.rochasoft;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class MeuResource
{

    /*
    @PostMapping("/api/clientes")
    public ResponseEntity salvar(@RequestBody Cliente cliente)
    {

        // service.save(cliente);

        return new ResponseEntity(cliente, HttpStatus.CREATED);

    }
    */


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente salvar(@RequestBody Cliente cliente)
    {

        // service.save(cliente);

        System.out.println(String.format("salvar - %s", cliente.getNome()));

        return cliente;

    }


    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id)
    {

        System.out.println(String.format("delete - %d", id));

        // service.delete(id)

    }


    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Cliente atualizar(@PathVariable Long id, @RequestBody Cliente cliente)
    {
        System.out.println(String.format("atualizar - %s", cliente.getNome()));
        // service.buscaPorId(id)
        // service.update(cliente)
        return cliente;
    }


    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Cliente obterDadosCliente(@PathVariable Long id)
    {

        System.out.println(String.format("obterDadosCliente - %d", id));

        Cliente cliente = new Cliente(id,"Anderson",
                                   "000000000000",
                                "Rua Teste",
                                "Bairro",
                                "Cidade",
                                "9 9999 9999");

        return cliente;

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Cliente> obterTodosClientes()
    {

        System.out.println("obterTodosClientes");

        List<Cliente> lista = new ArrayList<>();

        for (long i = 1; i <= 1000; i++ )
        {
            lista.add(new Cliente(i, "teste " + i, "12345678901", "endereço", "bairro", "cidade", "telefone"));
        }

        return lista;
    }

}
