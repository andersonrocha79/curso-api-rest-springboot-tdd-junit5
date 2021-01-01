package br.com.rochasoft;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CadastroPessoasTeste
{

    @Test
    @DisplayName("Deve criar o cadastro de pessoas")
    public void deveCriarCadastroPessoas()
    {

        // cenário e execução
        CadastroPessoas cadastro = new CadastroPessoas();

        // verificação
        Assertions.assertThat(cadastro.getPessoas()).isEmpty();


    }

    @Test
    @DisplayName("Deve adicionar uma pessoa")
    public void deveAdicionarUmaPessoa()
    {

        // cenário
        CadastroPessoas cadastro = new CadastroPessoas();
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Anderson Rocha");

        // execução
        cadastro.adicionar(pessoa);

        // verificação
        Assertions.assertThat(cadastro.getPessoas()).isNotEmpty()
                                                    .hasSize(1)
                                                    .contains(pessoa);

    }

    @Test                          // antigo (expected = PessoaSemNomeException.class)
    @DisplayName("Não deve permitir adicionar pessoa com nome vazio")
    public void naoDeveAdicionarPessoaNomeVazio()
    {

        // cenário
        CadastroPessoas cadastro = new CadastroPessoas();
        Pessoa pessoa = new Pessoa();

        // execução
        cadastro.adicionar(pessoa);

        // verificação
        // verifica se o erro foi gerado
        org.junit.jupiter.api.Assertions
                 .assertThrows(PessoaSemNomeException.class, () -> cadastro.adicionar(pessoa));

    }

    @Test
    @DisplayName("Deve remover uma pessoa")
    public void deveRemoverUmapessoa()
    {

        // cenário
        CadastroPessoas cadastro = new CadastroPessoas();
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Anderson Rocha");
        cadastro.adicionar(pessoa);

        // execução
        cadastro.remover(pessoa);

        // verificação
        Assertions.assertThat(cadastro.getPessoas()).isEmpty();

    }

    @Test
    @DisplayName("Deve gerar um erro ao tentar remover pessoa em uma lista vazia")
    public void deveLancarErroAoTentarRemoverPessoaInexistente()
    {

        // cenário
        CadastroPessoas cadastro = new CadastroPessoas();
        Pessoa pessoa = new Pessoa();

        // execução
        cadastro.remover(pessoa);

        // verificação
        // tentando excluir uma pessoa que não existe, irá gerar o erro

        org.junit.jupiter.api.Assertions
                .assertThrows(CadastroVazioException.class, () -> cadastro.remover(pessoa));

    }

}
