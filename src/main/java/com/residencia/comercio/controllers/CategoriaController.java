package com.residencia.comercio.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.residencia.comercio.dtos.CategoriaDTO;
import com.residencia.comercio.entities.Categoria;
import com.residencia.comercio.exceptions.NoSuchElementFoundException;
import com.residencia.comercio.services.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/categoria")
@Tag(name = "Categorias", description = "Endpoints")
public class CategoriaController {
	@Autowired
	CategoriaService categoriaService;

	@GetMapping
	@Operation(summary = "Listar todos os Categorias.")
	public ResponseEntity<List<Categoria>> findAllCategoria() {
		if (categoriaService.findAllCategoria().isEmpty() == true) {
			throw new NoSuchElementFoundException("Não há Categorias cadastradas no sistema");
		}
		else {
			return new ResponseEntity<>(categoriaService.findAllCategoria(), HttpStatus.OK);
		}
	}

	@GetMapping("/dto/{id}")
	@Operation(summary = "Listar uma Categoria pelo ID através de DTO.")
	public ResponseEntity<CategoriaDTO> findCategoriaDTOById(@PathVariable Integer id) {
		return new ResponseEntity<>(categoriaService.findCategoriaDTOById(id), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Listar uma Categoria pelo ID.")
	public ResponseEntity<Categoria> findCategoriaById(@PathVariable Integer id) {
		if (categoriaService.findCategoriaById(id) == null) {
			throw new NoSuchElementFoundException("Não foi encontrado umaa Categoria com o id " + id);
		} else {
			return new ResponseEntity<>(categoriaService.findCategoriaById(id), HttpStatus.OK);
		}
	}

	@PostMapping
	@Operation(summary = "Postar uma Categoria.")
	public ResponseEntity<Categoria> saveCategoria(@Valid @RequestBody Categoria categoria) {
		return new ResponseEntity<>(categoriaService.saveCategoria(categoria), HttpStatus.CREATED);
	}

	@PostMapping("/dto")
	@Operation(summary = "Postar uma Categoria através de DTO.")
	public ResponseEntity<CategoriaDTO> saveCategoriaDTO(@Valid @RequestBody CategoriaDTO categoriaDTO) {
		categoriaService.saveCategoriaDTO(categoriaDTO);
		return new ResponseEntity<>(categoriaDTO, HttpStatus.CREATED);
	}

	@PutMapping
	@Operation(summary = "Atualizar uma Categoria.")
	public ResponseEntity<Categoria> updateCategoria(@Valid @RequestBody Categoria categoria) {
		return new ResponseEntity<>(categoriaService.updateCategoria(categoria), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Deletar uma Categoria a partir de uma ID.")
	public ResponseEntity<String> deleteCategoria(@PathVariable Integer id) {
		if (categoriaService.findCategoriaById(id) == null) {
			throw new NoSuchElementFoundException("Não foi encontrado umaa Categoria com o id " + id);
		} else {
			categoriaService.deleteCategoria(id);
			return new ResponseEntity<>("Categoria deletada com sucesso", HttpStatus.OK);
		}
	}

}
