package com.trepudox.mercadinho.produto;

import com.trepudox.mercadinho.exception.CampoBlankException;
import com.trepudox.mercadinho.exception.produto.ProdutoNotFoundException;
import com.trepudox.mercadinho.model.Categoria;
import com.trepudox.mercadinho.model.Produto;
import com.trepudox.mercadinho.repository.ProdutoRepository;
import com.trepudox.mercadinho.service.ProdutoService;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DisplayName("Produto Service")
@ExtendWith(SpringExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepositoryMock;

    @InjectMocks
    private ProdutoService produtoService;

    @Nested
    class Find {

        @Test
        void testFindByIdInexistente() {
            when(produtoRepositoryMock.findById(9999999)).thenReturn(Optional.empty());

            try {
                Produto p = produtoService.findById(9999999);
                fail("Não lançou exceção!!!");
            } catch (ProdutoNotFoundException e) {
                assertEquals("Não existe nenhum produto com esse ID!", e.getMessage());
            }
        }

        @Test
        void testFindByIdNegativo() {
            try {
                Produto p = produtoService.findById(-1);
                fail("Não lançou exceção!!!");
            } catch (ProdutoNotFoundException e) {
                assertEquals("Não existem produtos com ID negativo!", e.getMessage());
            }
        }

        @Test
        void testFindByIdNormal() {
            Produto produtoEsperado = new Produto(10, "nomeProduto", 10.0, new Categoria());

            when(produtoRepositoryMock.findById(10)).thenReturn(Optional.of(produtoEsperado));

            try {
                Produto produtoRetornado = produtoService.findById(10);
                assertEquals(produtoEsperado, produtoRetornado);
            } catch (ProdutoNotFoundException e) {
                e.printStackTrace();
                fail("Não era pra lançar exceção!!!");
            }
        }

    }

    @Nested
    class FindAll {

        @Test
        void testFindAllVazio() {
            when(produtoRepositoryMock.findAll()).thenReturn(List.of());

            try {
                produtoService.findAll();
                fail("Não lançou exceção!!!!");
            } catch (ProdutoNotFoundException e) {
                assertEquals("Ainda não há produtos cadastrados!", e.getMessage());
            }
        }

        @Test
        void testFindAllNormal() {
            when(produtoRepositoryMock.findAll()).thenReturn(List.of(new Produto()));

            try {
                produtoService.findAll();
            } catch (ProdutoNotFoundException e) {
                fail("Não era pra lançar exceção!!!!");
            }
        }

        @Test
        void testFindAllByNomeVazio() {
            when(produtoRepositoryMock.findAllByNomeContainingIgnoreCase("produto")).thenReturn(List.of());

            try {
                produtoService.findAllByNome("produto");
                fail("Não lançou exceção!!!");
            } catch (ProdutoNotFoundException e) {
                assertEquals("Não há nenhum produto com esse nome!", e.getMessage());
            }
        }

        @Test
        void testFindAllByNomeNormal() {
            when(produtoRepositoryMock.findAllByNomeContainingIgnoreCase("produto")).thenReturn(List.of(new Produto()));

            try {
                produtoService.findAllByNome("produto");
            } catch (ProdutoNotFoundException e) {
                fail("Não era pra lançar exceção!!!");
            }
        }


    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Save {

        private List<Produto> listaProdutoInvalido() {
            List<Produto> lista = new ArrayList<>();

            lista.add(new Produto("nome", 1.0, null));
            lista.add(new Produto("nome", null, new Categoria()));
            lista.add(new Produto("nome", 0.0, new Categoria()));
            lista.add(new Produto("nome", -1.0, new Categoria()));
            lista.add(new Produto(null, 1.0, new Categoria()));
            lista.add(new Produto("", 1.0, new Categoria()));

            return lista;
        }

        @Test
        void testSaveValido() {
            Produto p = new Produto("nome", 1.0, new Categoria());

            when(produtoRepositoryMock.save(p)).thenReturn(p);

            try{
                assertEquals(p, produtoService.save(p));
            } catch (CampoBlankException e) {
                fail("Não era pra ter lançado exceção!!!");
            }
        }

        @ParameterizedTest(name = "Teste {index}")
        @MethodSource("listaProdutoInvalido")
        void testSaveInvalido(Produto p) {
            when(produtoRepositoryMock.save(p)).thenReturn(p);

            assertThrows(CampoBlankException.class, () -> produtoService.save(p));
        }

    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Update {

        private List<Produto> listaProdutoInvalido() {
            return List.of(new Produto(null, null, null),
                    new Produto("", 0.0, null),
                    new Produto("", -1.0, null),
                    new Produto("AAA", 10.0, null),
                    new Produto("AAAA", -1.0, new Categoria()),
                    new Produto("AAAA", 0.0, new Categoria()),
                    new Produto("", 10.0, new Categoria()),
                    new Produto(null, 5.0, new Categoria()));
        }

        private List<Produto> listaProdutoValido() {
            return List.of(new Produto("Nome1", 100.50, new Categoria(1, "nome1", "desc1", List.of())),
                    new Produto("Nome2", 88.99, new Categoria(2, "nome2", "desc2", List.of())),
                    new Produto("Nome3", 0.01, new Categoria(3, "nome3", "desc3", List.of(new Produto()))));
        }

        @BeforeAll
        void setup() {
            Produto p = new Produto(); p.setId(1);

            when(produtoRepositoryMock.findById(1)).thenReturn(Optional.of(p));
        }

        @ParameterizedTest(name = "Teste {index}")
        @MethodSource("listaProdutoInvalido")
        void testUpdateProdutoInvalido(Produto produto) {
            when(produtoRepositoryMock.save(produto)).thenReturn(produto);

            try {
                produtoService.update(1, produto);
                fail("Não lançou exceção!!!");
            } catch (ProdutoNotFoundException | CampoBlankException e) {
                assertEquals("Preencha os campos corretamente!", e.getMessage());
            }
        }

        @ParameterizedTest(name = "Teste {index}")
        @MethodSource("listaProdutoValido")
        void testUpdateProdutoValido(Produto produto) {
            when(produtoRepositoryMock.save(produto)).thenReturn(produto);

            Integer id = 1;
            Produto produtoEsperado = new Produto(id, produto.getNome(), produto.getPreco(), produto.getCategoria());

            try {
                Produto produtoRetornado = produtoService.update(id, produto);
                assertAll(() -> assertEquals(id, produtoRetornado.getId()),
                        () -> assertEquals(produtoEsperado.getNome(), produtoRetornado.getNome()),
                        () -> assertEquals(produtoEsperado.getPreco(), produtoRetornado.getPreco()),
                        () -> assertEquals(produtoEsperado.getCategoria(), produtoRetornado.getCategoria()));
            } catch (ProdutoNotFoundException | CampoBlankException e) {
                fail("Não era pra lançar exceção!!!");
            }
        }

    }

    @Nested
    class Delete {

        @Test
        void testDeleteIdInexistente() {
            try {
                produtoService.delete(999999);
                fail("Não lançou exceção!");
            } catch (ProdutoNotFoundException e) {
                assertEquals("Não existe nenhum produto com esse ID!", e.getMessage());
            }
        }

        @Test
        void testDeleteIdNegativo() {
            try {
                produtoService.delete(-1);
                fail("Não lançou exceção!!");
            } catch (ProdutoNotFoundException e) {
                assertEquals("Não existem produtos com ID negativo!", e.getMessage());
            }
        }

    }

}
