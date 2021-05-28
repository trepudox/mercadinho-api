package com.trepudox.mercadinho.categoria;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.trepudox.mercadinho.exception.categoria.CategoriaNotFoundException;
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
        void testFindById1() {
            when(categoriaRepositoryMock.findById(999999999)).thenReturn(Optional.empty());

            try{
                categoriaService.findById(9999999);
                fail("Não lançou exceção!");
            } catch (CategoriaNotFoundException e) {
                assertEquals("Não existe nenhuma categoria com esse ID!", e.getMessage());
            }
        }

        @Test
        void testFindById2() {
            try {
                categoriaService.findById(-1);
                fail("Não lançou exceção!!!");
            } catch (CategoriaNotFoundException e) {
                assertEquals("Não existem categorias com ID negativo!", e.getMessage());
            }
        }

    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class FindAll {

    }

}
