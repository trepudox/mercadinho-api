package com.trepudox.mercadinho.service;

import com.trepudox.mercadinho.exception.CampoBlankException;
import com.trepudox.mercadinho.exception.produto.ProdutoNotFoundException;
import com.trepudox.mercadinho.model.Produto;
import com.trepudox.mercadinho.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public Produto findById(Integer id) throws ProdutoNotFoundException {
        if (id < 0)
            throw new ProdutoNotFoundException("Não existem produtos com ID negativo!");

        Optional<Produto> p = produtoRepository.findById(id);

        if (!p.isPresent())
            throw new ProdutoNotFoundException("Não existe nenhum produto com esse ID!");

        return p.get();
    }

    public List<Produto> findAll() throws ProdutoNotFoundException {
        List<Produto> lista = produtoRepository.findAll();

        if (lista.isEmpty())
            throw new ProdutoNotFoundException("Ainda não há produtos cadastrados!");

        return lista;
    }

    public List<Produto> findAllByNome(String nome) throws ProdutoNotFoundException {
        List<Produto> lista = produtoRepository.findAllByNomeContainingIgnoreCase(nome);

        if (lista.isEmpty())
            throw new ProdutoNotFoundException("Não há nenhum produto com esse nome!");

        return lista;
    }

    public Produto save(Produto produto) throws CampoBlankException {
        if (produto.getCategoria() == null)
            throw new CampoBlankException("categoria");

        if (produto.getNome() == null)
            throw new CampoBlankException("nome");

        if (produto.getPreco() == null)
            throw new CampoBlankException("preco");


        return produtoRepository.save(produto);
    }

    public void delete(Integer id) throws ProdutoNotFoundException {
        Produto p = findById(id);

        produtoRepository.delete(p);
    }

}
