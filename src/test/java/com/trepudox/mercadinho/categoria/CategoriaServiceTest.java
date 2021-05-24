package com.trepudox.mercadinho.categoria;

import com.trepudox.mercadinho.repository.CategoriaRepository;
import com.trepudox.mercadinho.service.CategoriaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DisplayName("Categoria Service")
@ExtendWith(SpringExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepositoryMock;

    @InjectMocks
    private CategoriaService categoriaService;

}
