package br.com.rochasoft;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

// https://site.mockito.org/
// https://junit.org/junit5/

@ExtendWith(MockitoExtension.class)
public class MockitoTestes
{

    @Mock
    List<String> lista;

    @Test
    @DisplayName("Primeiro teste utilizando mockito")
    public void primeiroTesteMockito()
    {

        // List<String> lista = Mockito.mock(ArrayList.class); modelo1
        lista.add("teste1");
        lista.add("teste2");

        // quando utilizar o método lista.size irá retornar '20', conforme definido abaixo
        Mockito.when(lista.size()).thenReturn(20);

        int size = lista.size();

        lista.add("");

        Assertions.assertThat(size).isEqualTo(20);

        // verifica se o método size foi chamado na execução acima
        // útil quando tem condicionais e o método pode não ser chamado
        Mockito.verify(lista, Mockito.times(1)).size();
        // Mockito.verify(lista, Mockito.never()).size(); não pode ser executado o método size para passar no teste

        // verifica se os métodos foram executados na ordem especificada abaixo no mock 'lista'
        InOrder inOrder = Mockito.inOrder(lista);
        inOrder.verify(lista).size();
        inOrder.verify(lista).add("");

    }

}
