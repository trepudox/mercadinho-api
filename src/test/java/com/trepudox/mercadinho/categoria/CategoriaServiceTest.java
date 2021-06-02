package com.trepudox.mercadinho.categoria;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.trepudox.mercadinho.exception.categoria.CategoriaNotFoundException;
import com.trepudox.mercadinho.model.Categoria;
import com.trepudox.mercadinho.repository.CategoriaRepository;
import com.trepudox.mercadinho.service.CategoriaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@DisplayName("Categoria Service")
@ExtendWith(SpringExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepositoryMock;

    @InjectMocks
    private CategoriaService categoriaService;

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Find {

        @Test
        void testFindByIdInexistente() {
            when(categoriaRepositoryMock.findById(9999999)).thenReturn(Optional.empty());

            try{
                categoriaService.findById(9999999);
                fail("Não lançou exceção!");
            } catch (CategoriaNotFoundException e) {
                assertEquals("Não existe nenhuma categoria com esse ID!", e.getMessage());
            }
        }

        @Test
        void testFindByIdNegativo() {
            try {
                categoriaService.findById(-1);
                fail("Não lançou exceção!!!");
            } catch (CategoriaNotFoundException e) {
                assertEquals("Não existem categorias com ID negativo!", e.getMessage());
            }
        }

        @Test
        void testFindByIdNormal() {
            Categoria categoriaEsperada = new Categoria(10,"nomeCategoria", "descricaoCategoria");

            when(categoriaRepositoryMock.findById(10)).thenReturn(Optional.of(categoriaEsperada));

            try {
                Categoria categoriaRetornada = categoriaService.findById(10);
                assertEquals(categoriaEsperada, categoriaRetornada);
            } catch (CategoriaNotFoundException e) {
                e.printStackTrace();
                fail("Não era pra lançar exceção!");
            }
        }

    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class FindAll {

        @Test
        void testFindAllVazio() {
            when(categoriaRepositoryMock.findAll()).thenReturn(List.of());

            Exception e = assertThrows(CategoriaNotFoundException.class, () -> categoriaService.findAll());
            assertEquals("Ainda não há categorias cadastradas!", e.getMessage());
        }

        @Test
        void testFindAllNormal() {
            List<Categoria> resultadoEsperado = List.of(new Categoria(), new Categoria());

            when(categoriaRepositoryMock.findAll()).thenReturn(resultadoEsperado);

            List<Categoria> resultadoRetornado = assertDoesNotThrow(() -> categoriaService.findAll());
            assertEquals(resultadoEsperado, resultadoRetornado);
        }

        @Test
        void testFindAllByNomeVazio() {
            when(categoriaRepositoryMock.findByNomeContainingIgnoreCase("nome1")).thenReturn(List.of());

            Exception e = assertThrows(CategoriaNotFoundException.class, () -> categoriaService.findAllByNome("nome1"));
            assertEquals("Não há nenhuma categoria com esse nome!", e.getMessage());
        }

        @Test
        void testFindAllByNomeNormal() {
            List<Categoria> resultadoEsperado = List.of(new Categoria(), new Categoria(), new Categoria());

            when(categoriaRepositoryMock.findByNomeContainingIgnoreCase("nome2")).thenReturn(resultadoEsperado);

            List<Categoria> resultadoRetornado = assertDoesNotThrow(() -> categoriaService.findAllByNome("nome2"));
            assertEquals(resultadoEsperado, resultadoRetornado);
        }

    }

}
