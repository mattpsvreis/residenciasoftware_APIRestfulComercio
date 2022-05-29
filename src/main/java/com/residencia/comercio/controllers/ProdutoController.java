package com.residencia.comercio.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.residencia.comercio.dtos.ProdutoDTO;
import com.residencia.comercio.entities.Produto;
import com.residencia.comercio.exceptions.NoSuchElementFoundException;
import com.residencia.comercio.exceptions.NotNullException;
import com.residencia.comercio.services.ProdutoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/produto")
@Tag(name = "Produtos", description = "Endpoints")
public class ProdutoController {
	@Autowired
	ProdutoService produtoService;

	@GetMapping
	@Operation(summary = "Listar todos os Produtos.")
	public ResponseEntity<List<Produto>> findAllProduto() {
		if (produtoService.findAllProduto().isEmpty() == true) {
			throw new NoSuchElementFoundException("Não há Produtos cadastrados no sistema");
		} else {
			return new ResponseEntity<>(produtoService.findAllProduto(), HttpStatus.OK);
		}
	}

	@GetMapping("/dto/{id}")
	@Operation(summary = "Listar um Produto pelo ID através de DTO.")
	public ResponseEntity<ProdutoDTO> findProdutoDTOById(@PathVariable Integer id) {
		return new ResponseEntity<>(produtoService.findProdutoDTOById(id), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Listar um Produto pelo ID.")
	public ResponseEntity<Produto> findProdutoById(@PathVariable Integer id) {
		if (produtoService.findProdutoById(id) == null) {
			throw new NoSuchElementFoundException("Não foi encontrado um Produto com o id " + id);
		} else {
			return new ResponseEntity<>(produtoService.findProdutoById(id), HttpStatus.OK);
		}
	}

	@PostMapping
	@Operation(summary = "Postar um Produto sem foto.")
	public ResponseEntity<Produto> saveProduto(@Valid @RequestBody Produto produto) {
		
		if (produto.getFornecedor().getIdFornecedor() == null) {
			throw new NotNullException("ID do Fornecedor não pode ser nulo.");
		}
		
		if (produto.getCategoria().getIdCategoria() == null) {
			throw new NotNullException("ID da Categoria não pode ser nulo.");
		}
		
		return new ResponseEntity<>(produtoService.saveProduto(produto), HttpStatus.CREATED);
	}

	@PostMapping(value = "/com-foto", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	@Operation(summary = "Postar um Produto com foto.")
	public ResponseEntity<Produto> saveProdutoWithImage(@RequestPart("produto") String produto,
			@RequestPart("file") MultipartFile file) {
		return new ResponseEntity<>(produtoService.saveProdutoWithImage(produto, file), HttpStatus.CREATED);
	}

	@PostMapping("/dto")
	@Operation(summary = "Postar um Produto sem foto através de DTO.")
	public ResponseEntity<ProdutoDTO> saveProdutoDTO(@Valid @RequestBody ProdutoDTO produtoDTO) {
		produtoService.saveProdutoDTO(produtoDTO);
		return new ResponseEntity<>(produtoDTO, HttpStatus.CREATED);
	}

	@PutMapping
	@Operation(summary = "Atualizar um Produto.")
	public ResponseEntity<Produto> updateProduto(@Valid @RequestBody Produto produto) {
		return new ResponseEntity<>(produtoService.updateProduto(produto), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Deletar um Produto.")
	public ResponseEntity<String> deleteProduto(@PathVariable Integer id) {
		if (produtoService.findProdutoById(id) == null) {
			throw new NoSuchElementFoundException("Não foi encontrado um Produto com o id " + id);
		} else {
			produtoService.deleteProduto(id);
			return new ResponseEntity<>("Produto deletado com sucesso", HttpStatus.OK);
		}
	}

}
