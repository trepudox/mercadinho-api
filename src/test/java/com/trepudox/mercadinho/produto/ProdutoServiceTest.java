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
        void testFindById1() {
            try {
                Produto p = produtoService.findById(9999999);
                fail("Não lançou exceção!!!");
            } catch (ProdutoNotFoundException e) {
                assertEquals("Não existe nenhum produto com esse ID!", e.getMessage());
            }
        }

        @Test
        void testFindById2() {
            try {
                Produto p = produtoService.findById(-1);
                fail("Não lançou exceção!!!");
            } catch (ProdutoNotFoundException e) {
                assertEquals("Não existem produtos com ID negativo!", e.getMessage());
            }
        }

    }

    @Nested
    class FindAll {

        @Test
        void testFindAll1() {
            when(produtoRepositoryMock.findAll()).thenReturn(List.of());

            try {
                produtoService.findAll();
                fail("Não lançou exceção!!!!");
            } catch (ProdutoNotFoundException e) {
                assertEquals("Ainda não há produtos cadastrados!", e.getMessage());
            }
        }

        @Test
        void testFindAll2() {
            when(produtoRepositoryMock.findAll()).thenReturn(List.of(new Produto()));

            try {
                produtoService.findAll();
            } catch (ProdutoNotFoundException e) {
                fail("Não era pra lançar exceção!!!!");
            }
        }

        @Test
        void testFindAllByNome1() {
            when(produtoRepositoryMock.findAllByNomeContainingIgnoreCase("produto")).thenReturn(List.of(new Produto()));

            try {
                produtoService.findAllByNome("produto");
            } catch (ProdutoNotFoundException e) {
                fail("Não era pra lançar exceção!!!");
            }
        }

        @Test
        void testFindAllByNome2() {
            when(produtoRepositoryMock.findAllByNomeContainingIgnoreCase("produto")).thenReturn(List.of());

            try {
                produtoService.findAllByNome("produto");
                fail("Não lançou exceção!!!");
            } catch (ProdutoNotFoundException e) {
                assertEquals("Não há nenhum produto com esse nome!", e.getMessage());
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

        // gambiarra
        private Produto updateProduto(Produto produto) throws CampoBlankException {
            Produto p = new Produto(1, "nome", 1.0, new Categoria());

            boolean categoriaIsNull = produto.getCategoria() == null;
            boolean nomeIsBlankOrNull = produto.getNome() == null || produto.getNome().isBlank();
            boolean precoIsInvalid = produto.getPreco() == null || produto.getPreco().isNaN() || produto.getPreco() <= 0;

            if (categoriaIsNull && nomeIsBlankOrNull && precoIsInvalid)
                throw new CampoBlankException("Preencha os campos corretamente!");

            if (!categoriaIsNull)
                p.setCategoria(produto.getCategoria());

            if (!nomeIsBlankOrNull)
                p.setNome(produto.getNome());

            if (!precoIsInvalid)
                p.setPreco(produto.getPreco());

            return p;
        }

        private List<Produto> listaProdutoInvalido() {
            return List.of(new Produto(null, null, null),
                    new Produto("", 0.0, null),
                    new Produto("", -1.0, null));
        }

//        private List<Produto> listaProdutoValido() {
//            return List.of(new Produto("produto", 1.0, new Categoria()),
//                    new Produto("produto2", 0.0, new Categoria()),
//                    new Produto("produto3", 10.0, null),
//                    new Produto("", 999.0, null),
//                    new Produto(null, null, new Categoria()));
//        }

        @BeforeAll
        private void setupInicial() {
            when(produtoRepositoryMock.findById(1)).thenReturn(Optional.of(new Produto(1, "nome", 1.0, new Categoria())));
            when(produtoRepositoryMock.findById(9999999)).thenReturn(Optional.empty());

            // when(produtoRepositoryMock.save(p)).thenReturn(p);
        }

        @Test
        void testUpdateIdNaoEncontrado() {
            Produto p = new Produto();
            when(produtoRepositoryMock.save(p)).thenReturn(p);

            assertThrows(ProdutoNotFoundException.class, () -> produtoService.update(9999999, p));
        }

        @Test
        void testUpdateTodosNull() {
            Produto p = new Produto();
            when(produtoRepositoryMock.save(p)).thenReturn(p);

            try {
                produtoService.update(1, p);
                fail("Não lançou exceção!!!!");
            } catch (CampoBlankException e) {
                assertEquals("Preencha os campos corretamente!", e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                fail("Erro inesperado!!!!");
            }
        }

        @ParameterizedTest(name = "Teste {index}")
        @MethodSource("listaProdutoInvalido")
        void testUpdateInvalido(Produto p) {
            when(produtoRepositoryMock.save(p)).thenReturn(p);

            try {
                produtoService.update(1, p);
                fail("Não lançou exceção!!!!");
            } catch (CampoBlankException e) {
                assertEquals("Preencha os campos corretamente!", e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                fail("Erro inesperado!!!");
            }
        }

        @Test
        void testUpdateValido1() {
            Produto p = new Produto("produto2", 0.0, new Categoria());

            try {
                produtoService.update(1, p);
                Produto pSalvo = updateProduto(p);
                assertAll(() -> assertEquals(p.getNome(), pSalvo.getNome()),
                        () -> assertNotEquals(p.getPreco(), pSalvo.getPreco()),
                        () -> assertEquals(p.getCategoria(), pSalvo.getCategoria()));
            } catch (ProdutoNotFoundException | CampoBlankException e) {
                e.printStackTrace();
                fail("Não era pra lançar exceção!!!!");
            }
        }

        @Test
        void testUpdateValido2() {
            Produto p = new Produto("produto", 1.0, new Categoria("nome", "desc"));
            when(produtoRepositoryMock.save(p)).thenReturn(p);

            try {
                produtoService.update(1, p);
                Produto pSalvo = updateProduto(p);
                assertAll(() -> assertEquals(p.getNome(), pSalvo.getNome()),
                        () -> assertEquals(p.getPreco(), pSalvo.getPreco()),
                        () -> assertEquals(p.getCategoria(), pSalvo.getCategoria()));
            } catch (ProdutoNotFoundException | CampoBlankException e) {
                e.printStackTrace();
                fail("Não era pra lançar exceção!!!!");
            }
        }

        @Test
        void testUpdateValido3() {
            Produto p = new Produto("produto3", 10.0, null);
            when(produtoRepositoryMock.save(p)).thenReturn(p);

            try {
                produtoService.update(1, p);
                Produto pSalvo = updateProduto(p);
                assertAll(() -> assertEquals(p.getNome(), pSalvo.getNome()),
                        () -> assertEquals(p.getPreco(), pSalvo.getPreco()),
                        () -> assertNotEquals(p.getCategoria(), pSalvo.getCategoria()));
            } catch (ProdutoNotFoundException | CampoBlankException e) {
                e.printStackTrace();
                fail("Não era pra lançar exceção!!!!");
            }
        }

        @Test
        void testUpdateValido4() {
            Produto p = new Produto("", 999.0, null);
            when(produtoRepositoryMock.save(p)).thenReturn(p);

            try {
                produtoService.update(1, p);
                Produto pSalvo = updateProduto(p);
                assertAll(() -> assertNotEquals(p.getNome(), pSalvo.getNome()),
                        () -> assertEquals(p.getPreco(), pSalvo.getPreco()),
                        () -> assertNotEquals(p.getCategoria(), pSalvo.getCategoria()));
            } catch (ProdutoNotFoundException | CampoBlankException e) {
                e.printStackTrace();
                fail("Não era pra lançar exceção!!!!");
            }
        }

        @Test
        void testUpdateValido5() {
            Produto p = new Produto(null, null, new Categoria());
            when(produtoRepositoryMock.save(p)).thenReturn(p);

            try {
                produtoService.update(1, p);
                Produto pSalvo = updateProduto(p);
                assertAll(() -> assertNotEquals(p.getNome(), pSalvo.getNome()),
                        () -> assertNotEquals(p.getPreco(), pSalvo.getPreco()),
                        () -> assertEquals(p.getCategoria(), pSalvo.getCategoria()));
            } catch (ProdutoNotFoundException | CampoBlankException e) {
                e.printStackTrace();
                fail("Não era pra lançar exceção!!!!");
            }
        }

    }

    @Nested
    class Delete {

        @Test
        void testDelete1() {
            try {
                produtoService.delete(999999);
                fail("Não lançou exceção!");
            } catch (ProdutoNotFoundException e) {
                assertEquals("Não existe nenhum produto com esse ID!", e.getMessage());
            }
        }

        @Test
        void testDelete2() {
            try {
                produtoService.delete(-1);
                fail("Não lançou exceção!!");
            } catch (ProdutoNotFoundException e) {
                assertEquals("Não existem produtos com ID negativo!", e.getMessage());
            }
        }

    }

}
