package com.trepudox.mercadinho.controller;

import com.trepudox.mercadinho.exception.CampoBlankException;
import com.trepudox.mercadinho.exception.categoria.CategoriaNotFoundException;
import com.trepudox.mercadinho.model.Categoria;
import com.trepudox.mercadinho.service.CategoriaService;
import com.trepudox.mercadinho.util.PayloadMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/all")
    public ResponseEntity<List<Categoria>> getAll() throws CategoriaNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(categoriaService.findAll());
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Categoria>> getByNome(@PathVariable(name = "nome") String nome) throws CategoriaNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(categoriaService.findAllByNome(nome));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Categoria> getById(@PathVariable(name = "id") Integer id) throws CategoriaNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(categoriaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Categoria> save(@RequestBody Categoria categoria) throws CampoBlankException {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.save(categoria));
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<Categoria> update(@PathVariable(name = "id") Integer id, @RequestBody Categoria categoria)
            throws CategoriaNotFoundException, CampoBlankException {
        return ResponseEntity.status(HttpStatus.OK).body(categoriaService.update(id, categoria));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<PayloadMessage> delete(@PathVariable(name = "id") Integer id) throws CategoriaNotFoundException {
        categoriaService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new PayloadMessage("Produto deletado com sucesso", HttpStatus.OK));
    }
}
