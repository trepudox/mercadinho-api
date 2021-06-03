package com.trepudox.mercadinho.categoria;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.trepudox.mercadinho.exception.CampoBlankException;
import com.trepudox.mercadinho.exception.categoria.CategoriaNotFoundException;
import com.trepudox.mercadinho.model.Categoria;
import com.trepudox.mercadinho.model.Produto;
import com.trepudox.mercadinho.repository.CategoriaRepository;
import com.trepudox.mercadinho.service.CategoriaService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Save {

        private List<Categoria> categoriasInvalidasNome() {
            return List.of(new Categoria("", "descricao1"),
                    new Categoria(null, "descricao2"),
                    new Categoria("", ""));
        }

        private List<Categoria> categoriasInvalidasDescricao() {
            return List.of(new Categoria("nome1", ""),
                    new Categoria("nome2", null));
        }

        @ParameterizedTest(name = "Teste {index}")
        @MethodSource("categoriasInvalidasNome")
        void testSaveCategoriaComNomeInvalido(Categoria c) {
            when(categoriaRepositoryMock.save(c)).thenReturn(c);

            Exception e = assertThrows(CampoBlankException.class, () -> categoriaService.save(c));
            assertEquals("O campo 'nome' não pode estar nulo ou em branco!", e.getMessage());
        }

        @ParameterizedTest(name = "Teste {index}")
        @MethodSource("categoriasInvalidasDescricao")
        void testSaveCategoriaComDescricaoInvalida(Categoria c) {
            when(categoriaRepositoryMock.save(c)).thenReturn(c);

            Exception e = assertThrows(CampoBlankException.class, () -> categoriaService.save(c));
            assertEquals("O campo 'descricao' não pode estar nulo ou em branco!", e.getMessage());
        }

    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Update {

        private List<Categoria> categoriasInvalidas() {
            return List.of(new Categoria(1, null, null), new Categoria(1,"teste", null),
                    new Categoria(1, null, "teste"), new Categoria(1, "", ""),
                    new Categoria(1, "teste", ""), new Categoria(1, "", "teste"));
        }

        private List<Categoria> categoriasValidas() {
            return List.of(new Categoria(1, "nome valido", "descricao valida"),
                    new Categoria(1, "teste", "teste"));
        }

        @BeforeAll
        void setup() {
            Categoria c = Categoria.builder().id(1).produtos(List.of(new Produto())).build();

            when(categoriaRepositoryMock.findById(1)).thenReturn(Optional.of(c));
        }

        @ParameterizedTest(name = "Teste {index}")
        @MethodSource("categoriasInvalidas")
        void testUpdateCategoriasInvalidas(Categoria categoria) {
            when(categoriaRepositoryMock.save(categoria)).thenReturn(categoria);

            Exception e = assertThrows(CampoBlankException.class, () -> categoriaService.update(1, categoria));
            assertEquals("Preencha os campos corretamente!", e.getMessage());
        }

        @ParameterizedTest(name = "Teste {index}")
        @MethodSource("categoriasValidas")
        void testUpdateCategoriasValidas(Categoria categoria) {
            when(categoriaRepositoryMock.save(categoria)).thenReturn(categoria);

            Integer id = 1;
            Categoria categoriaEsperada = new Categoria(id, categoria.getNome(), categoria.getDescricao(), List.of(new Produto()));

            Categoria categoriaRetornada = assertDoesNotThrow(() -> categoriaService.update(id, categoria));
            assertEquals(categoriaEsperada, categoriaRetornada);
        }

    }

    @Nested
    class Delete {

        @Test
        void testDeleteIdInexistente() {
            when(categoriaRepositoryMock.findById(9999999)).thenReturn(Optional.empty());

            Exception e = assertThrows(CategoriaNotFoundException.class, () -> categoriaService.delete(9999999));
            assertEquals("Não existe nenhuma categoria com esse ID!", e.getMessage());
        }

        @Test
        void testDeleteIdNegativo() {
            Exception e = assertThrows(CategoriaNotFoundException.class, () -> categoriaService.delete(-1));
            assertEquals("Não existem categorias com ID negativo!", e.getMessage());
        }

    }

}
