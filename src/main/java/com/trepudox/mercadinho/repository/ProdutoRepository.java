package com.trepudox.mercadinho.repository;

import com.trepudox.mercadinho.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    List<Produto> findAllByNomeContainingIgnoreCase(String nome);
}
