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

        if (p.isEmpty())
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
            throw new CampoBlankException("O campo 'categoria' não pode estar nulo ou em branco!");

        if (produto.getNome() == null || produto.getNome().isBlank())
            throw new CampoBlankException("O campo 'nome' não pode estar nulo ou em branco!");

        if (produto.getPreco() == null || produto.getPreco().isNaN() || produto.getPreco() <= 0)
            throw new CampoBlankException("O campo 'preco' precisa ser preenchido corretamente!");

        produto.setId(null);

        return produtoRepository.save(produto);
    }

    public Produto update(Integer id, Produto produto) throws ProdutoNotFoundException, CampoBlankException {
        Produto p = findById(id);

        boolean categoriaIsNull = produto.getCategoria() == null;
        boolean nomeIsBlankOrNull = produto.getNome() == null || produto.getNome().isBlank();
        boolean precoIsInvalid = produto.getPreco() == null || produto.getPreco().isNaN() || produto.getPreco() <= 0;

        if (categoriaIsNull || nomeIsBlankOrNull || precoIsInvalid)
            throw new CampoBlankException("Preencha os campos corretamente!");

        produto.setId(p.getId());

        return produtoRepository.save(produto);
    }

    public void delete(Integer id) throws ProdutoNotFoundException {
        Produto p = findById(id);

        produtoRepository.delete(p);
    }

}
