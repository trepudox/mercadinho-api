package com.trepudox.mercadinho.controller;

import com.trepudox.mercadinho.exception.CampoBlankException;
import com.trepudox.mercadinho.exception.produto.ProdutoNotFoundException;
import com.trepudox.mercadinho.model.Produto;
import com.trepudox.mercadinho.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<Produto> getById(@RequestParam(name = "id") Integer id) throws ProdutoNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Produto> postProduto(@RequestBody Produto produto) throws CampoBlankException {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.save(produto));
    }
}
