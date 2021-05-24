package com.trepudox.mercadinho.produto;

import com.trepudox.mercadinho.controller.ProdutoController;
import com.trepudox.mercadinho.service.ProdutoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DisplayName("Produto Controller")
@ExtendWith(SpringExtension.class)
class ProdutoControllerTest {

    @Mock
    private ProdutoService produtoServiceMock;

    @InjectMocks
    private ProdutoController produtoController;

    @Nested
    @DisplayName("GetById: ")
    class GetById {

        @Test
        void testGetById1() {

        }

    }

}
