package br.com.rochasoft;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// https://joel-costigliola.github.io/assertj/assertj-core-quick-start.html

@ExtendWith(MockitoExtension.class)
public class PrimeiroTeste
{

    @Mock
    Calculadora calc;

    int numero1;
    int numero2;
    int resultado;

    @BeforeEach
    @DisplayName("Cria objetos antes de cada teste")
    public void setup()
    {
        // código será executado antes de cada método de teste
        calc = new Calculadora();
    }

    @Test
    @DisplayName("Deve somar 2 números")
    public void deveSomar2Numeros()
    {

        // *** estrutura do teste ***********************

        // cenário
        numero1 = 10;
        numero2 = 5;

        // execução
        resultado = calc.somar(numero1, numero2);

        // verificações
        // Assert.assertEquals(15, resultado);
        // Assertions.assertThat(resultado).isBetween(14, 16);
        // Assertions.assertThat(resultado).isGreaterThan(10);
        Assertions.assertThat(resultado).isEqualTo(15);

    }

    @Test
    @DisplayName("Deve subtrair 2 números")
    public void deveSubtrair2Numeros()
    {

        // *** estrutura do teste ***********************

        // cenário
        numero1 = 100;
        numero2 = 50;

        // execução
        resultado = calc.subtrair(numero1, numero2);

        // verificações
        Assertions.assertThat(resultado).isEqualTo(50);

    }

    @Test
    @DisplayName("Deve multiplicar 2 números")
    public void deveMultiplicar2Numeros()
    {

        // *** estrutura do teste ***********************

        // cenário
        numero1 = 5;
        numero2 = 6;

        // execução
        resultado = calc.multiplicar(numero1, numero2);

        // verificações
        Assertions.assertThat(resultado).isEqualTo(30);

    }

    @Test
    @DisplayName("Deve dividir 2 números")
    public void deveDividir2Numeros()
    {

        // *** estrutura do teste ***********************

        // cenário
        numero1 = 30;
        numero2 = 5;

        // execução
        resultado = calc.dividir(numero1, numero2);

        // verificações
        Assertions.assertThat(resultado).isEqualTo(6);

    }

    @Test
    @DisplayName("Não deve somar números negativos")
    public void naoDeveSomarNumerosNegativos()
    {

        // *** estrutura do teste ***********************

        // cenário
        numero1 = -10;
        numero2 = 5;

        // execução
        resultado = calc.somar(numero1, numero2);

        // verificações
        Assertions.assertThat(resultado).isEqualTo(15);

        // verificação
        org.junit.jupiter.api.Assertions
                .assertThrows(RuntimeException.class, () -> calc.somar(numero1, numero2));


    }

    // para o teste retornar 'sucesso' a função deve retornar o erro da classe abaixo
    @Test
    @DisplayName("Não deve dividir um valor por zero")
    public void naoDeveDividirPorZero()
    {

        // *** estrutura do teste ***********************

        // cenário
        numero1 = 30;
        numero2 = 0;

        // execução
        calc.dividir(numero1, numero2);

        // verificação
        org.junit.jupiter.api.Assertions
                 .assertThrows(RuntimeException.class, () -> calc.dividir(numero1, numero2));

    }

}

class Calculadora
{

    int somar(int num1, int num2)
    {

        if (num1 < 0 || num2 < 0)
            throw new RuntimeException("não é permitido somar números negativos.");

        return num1 + num2;

    }

    int subtrair(int num1, int num2)
    {

        return num1 - num2;

    }

    int multiplicar(int num1, int num2)
    {

        return num1 * num2;

    }

    int dividir(int num1, int num2)
    {

        if (num2 == 0)
            throw new RuntimeException("não é permitido divisão por zero.");

        return num1 / num2;

    }

}
