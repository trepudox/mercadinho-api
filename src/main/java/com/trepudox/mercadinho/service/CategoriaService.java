package com.trepudox.mercadinho.service;

import com.trepudox.mercadinho.exception.CampoBlankException;
import com.trepudox.mercadinho.exception.categoria.CategoriaNotFoundException;
import com.trepudox.mercadinho.model.Categoria;
import com.trepudox.mercadinho.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Categoria findById(Integer id) throws CategoriaNotFoundException {
        if (id < 0)
            throw new CategoriaNotFoundException("Não existem categorias com ID negativo!");

        Optional<Categoria> c = categoriaRepository.findById(id);

        if (c.isEmpty())
            throw new CategoriaNotFoundException("Não existe nenhuma categoria com esse ID!");

        return c.get();
    }

    public List<Categoria> findAll() throws CategoriaNotFoundException {
        List<Categoria> lista = categoriaRepository.findAll();

        if (lista.isEmpty())
            throw new CategoriaNotFoundException("Ainda não há categorias cadastradas!");

        return lista;
    }

    public List<Categoria> findAllByNome(String nome) throws CategoriaNotFoundException {
        List<Categoria> lista = categoriaRepository.findByNomeContainingIgnoreCase(nome);

        if (lista.isEmpty())
            throw new CategoriaNotFoundException("Não há nenhuma categoria com esse nome!");

        return lista;
    }

    public Categoria save(Categoria categoria) throws CampoBlankException {
        if (categoria.getNome().isBlank())
            throw new CampoBlankException("O campo 'nome' não pode estar nulo ou em branco!");

        if (categoria.getDescricao().isBlank())
            throw new CampoBlankException("O campo 'descricao' não pode estar nulo ou em branco!");

        return categoriaRepository.save(categoria);
    }

    public Categoria update(Integer id, Categoria categoria) throws CategoriaNotFoundException, CampoBlankException {
        Categoria c = findById(id);

        boolean nomeIsBlank = categoria.getNome() == null || categoria.getNome().isBlank();
        boolean descricaoIsBlank = categoria.getDescricao() == null || categoria.getDescricao().isBlank();

        if (nomeIsBlank && descricaoIsBlank)
            throw new CampoBlankException("Preencha os campos corretamente!");

        categoria.setId(c.getId());
        categoria.setProdutos(c.getProdutos());

        return categoriaRepository.save(c);
    }

}
