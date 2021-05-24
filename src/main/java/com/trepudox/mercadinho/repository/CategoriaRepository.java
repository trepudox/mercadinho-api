package com.trepudox.mercadinho.repository;

import com.trepudox.mercadinho.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    List<Categoria> findByNomeContainingIgnoreCase(String nome);
}
