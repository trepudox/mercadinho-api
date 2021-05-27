package com.trepudox.mercadinho.controller;

import com.trepudox.mercadinho.exception.CampoBlankException;
import com.trepudox.mercadinho.exception.produto.ProdutoNotFoundException;
import com.trepudox.mercadinho.model.Produto;
import com.trepudox.mercadinho.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping("/id/{id}")
    public ResponseEntity<Produto> getById(@PathVariable(name = "id") Integer id) throws ProdutoNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.findById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Produto>> getAll() throws ProdutoNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.findAll());
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Produto>> getByNome(@PathVariable(name = "nome") String nome) throws ProdutoNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.findAllByNome(nome));
    }

    @PostMapping
    public ResponseEntity<Produto> postProduto(@RequestBody Produto produto) throws CampoBlankException {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.save(produto));
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<Produto> putProduto(@PathVariable Integer id, @RequestBody Produto produto) throws ProdutoNotFoundException, CampoBlankException {
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.update(id, produto));
    }
}
