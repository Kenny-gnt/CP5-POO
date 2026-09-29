package br.com.fiap.petfiap.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Testes unitarios do model: sem banco, sem Spring (Aula 15).
public class BanhoTest {

    private Banho banhoDoRex() {
        return new Banho(1, "Rex", "PEQUENO", "Ana", LocalDateTime.of(2026, 10, 1, 10, 0));
    }

    @Test
    public void deveAcumular20PontosDeFidelidade() {
        // Act
        int pontos = banhoDoRex().calcularPontosFidelidade();

        // Assert
        assertEquals(20, pontos);
    }

    @Test
    public void deveDurar45Minutos() {
        // Act
        int duracao = banhoDoRex().getDuracaoMinutos();

        // Assert
        assertEquals(45, duracao);
    }
    
    @Test
    public void deveCalcularPrecoDeAcordoComOPorte() {
        // Arrange
        Banho pequeno = new Banho(
                1, "Rex", "PEQUENO", "Ana",
                LocalDateTime.of(2026, 10, 1, 10, 0)
        );

        Banho medio = new Banho(
                2, "Thor", "MEDIO", "Joao",
                LocalDateTime.of(2026, 10, 1, 11, 0)
        );

        Banho grande = new Banho(
                3, "Mel", "GRANDE", "Maria",
                LocalDateTime.of(2026, 10, 1, 12, 0)
        );

        // Act
        double precoPequeno = pequeno.calcularPreco();
        double precoMedio = medio.calcularPreco();
        double precoGrande = grande.calcularPreco();

        // Assert
        assertEquals(60.0, precoPequeno);
        assertEquals(80.0, precoMedio);
        assertEquals(100.0, precoGrande);
    }
}
