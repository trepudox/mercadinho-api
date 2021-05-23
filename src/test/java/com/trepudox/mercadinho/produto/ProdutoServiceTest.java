package com.trepudox.mercadinho.produto;

import com.trepudox.mercadinho.exception.CampoBlankException;
import com.trepudox.mercadinho.exception.produto.ProdutoNotFoundException;
import com.trepudox.mercadinho.model.Categoria;
import com.trepudox.mercadinho.model.Produto;
import com.trepudox.mercadinho.repository.ProdutoRepository;
import com.trepudox.mercadinho.service.ProdutoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@DisplayName("Produto Service")
@ExtendWith(SpringExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepositoryMock;

    @InjectMocks
    private ProdutoService produtoService;

    @Nested
    @DisplayName("FindById: ")
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
    @DisplayName("FindAll: ")
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
    @DisplayName("Save: ")
    class Save {

        @Test
        void testSave1() {
            Produto p = new Produto();
            p.setCategoria(new Categoria());
            p.setNome("nome"); p.setPreco(1.0);

            when(produtoRepositoryMock.save(p)).thenReturn(p);

            try{
                assertEquals(p, produtoService.save(p));
            } catch (CampoBlankException e) {
                fail("Não era pra ter lançado exceção!!!");
            }
        }

        @Test
        void testSave2() {
            Produto p = new Produto();
            p.setNome("nome"); p.setPreco(1.0);

            when(produtoRepositoryMock.save(p)).thenReturn(p);

            try {
                produtoService.save(p);
                fail("Era pra ter lançado exceção!!!");
            } catch (CampoBlankException e) {
                assertEquals("O campo 'categoria' não pode estar nulo ou em branco!", e.getMessage());
            }
        }

        @Test
        void testSave3() {
            Produto p = new Produto();
            p.setCategoria(new Categoria());
            p.setNome("nome");

            when(produtoRepositoryMock.save(p)).thenReturn(p);

            try {
                produtoService.save(p);
                fail("Era pra ter lançado exceção!!!");
            } catch (CampoBlankException e) {
                assertEquals("O campo 'preco' não pode estar nulo ou em branco!", e.getMessage());
            }
        }

        @Test
        void testSave4() {
            Produto p = new Produto();
            p.setCategoria(new Categoria());
            p.setPreco(1.0);

            when(produtoRepositoryMock.save(p)).thenReturn(p);

            try {
                produtoService.save(p);
                fail("Era pra ter lançado exceção!!!");
            } catch (CampoBlankException e) {
                assertEquals("O campo 'nome' não pode estar nulo ou em branco!", e.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("Delete: ")
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
