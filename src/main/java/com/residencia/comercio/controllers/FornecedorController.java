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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.residencia.comercio.dtos.FornecedorDTO;
import com.residencia.comercio.entities.Fornecedor;
import com.residencia.comercio.exceptions.CNPJException;
import com.residencia.comercio.exceptions.NoSuchElementFoundException;
import com.residencia.comercio.exceptions.NotNullException;
import com.residencia.comercio.services.FornecedorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/fornecedor")
@Tag(name = "Fornecedores", description = "Endpoints")
public class FornecedorController {
	@Autowired
	FornecedorService fornecedorService;
	
	@GetMapping
	@Operation(summary = "Listar todos os Fornecedores.")
	public ResponseEntity<List<Fornecedor>> findAllFornecedor() {
		if (fornecedorService.findAllFornecedor().isEmpty() == true) {
			throw new NoSuchElementFoundException("Não há Fornecedores cadastrados no sistema");
		} else {
			return new ResponseEntity<>(fornecedorService.findAllFornecedor(), HttpStatus.OK);
		}
	}

	@GetMapping("/dto/{id}")
	@Operation(summary = "Listar um Fornecedor pelo ID através de DTO.")
	public ResponseEntity<FornecedorDTO> findFornecedorDTOById(@PathVariable Integer id) {
		return new ResponseEntity<>(fornecedorService.findFornecedorDTOById(id), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Listar um Fornecedor pelo ID.")
	public ResponseEntity<Fornecedor> findFornecedorById(@PathVariable Integer id) {
		if (fornecedorService.findFornecedorById(id) == null) {
			throw new NoSuchElementFoundException("Não foi encontrado Fornecedor com o id " + id);
		} else {
			return new ResponseEntity<>(fornecedorService.findFornecedorById(id), HttpStatus.OK);
		}
	}

	@PostMapping
	@Operation(summary = "Postar um Fornecedor através de consumo de API Externa da Receita Federal.")
	public ResponseEntity<Fornecedor> saveFornecedor(@RequestParam String cnpj) {
		if (cnpj == null) {
			throw new NotNullException("CNPJ não pode ser nulo.");
		}
		
		if (cnpj.length() != 14) {
			throw new CNPJException("CNPJ deve ter 14 digitos. (Sem pontos, traços, ou barras.)");
		}
		
		return new ResponseEntity<>(
				fornecedorService.saveFornecedor(
						fornecedorService.CnpjDTOtoFornecedor(fornecedorService.getCnpjDTOFromExternal(cnpj))),
				HttpStatus.CREATED);
	}

	@PostMapping("/completo")
	@Operation(summary = "Postar um Fornecedor manualmente passando todos os dados necessários.")
	public ResponseEntity<Fornecedor> saveFornecedorCompleto(@Valid @RequestBody Fornecedor fornecedor) {
		if (!fornecedorService.CNPJValidFormatted(fornecedor.getCnpj())) {
			throw new CNPJException("CNPJ deve ter 18 digitos incluindo pontos, traços, e barras.");
		}
		
		return new ResponseEntity<>(fornecedorService.saveFornecedor(fornecedor), HttpStatus.CREATED);
	}

	@PostMapping("/dto")
	@Operation(summary = "Postar um Fornecedor manualmente passando todos os dados necessários através de DTO.")
	public ResponseEntity<FornecedorDTO> saveFornecedorDTO(@Valid @RequestBody FornecedorDTO fornecedorDTO) {
		fornecedorService.saveFornecedorDTO(fornecedorDTO);
		return new ResponseEntity<>(fornecedorDTO, HttpStatus.CREATED);
	}

	@PutMapping
	@Operation(summary = "Atualizar um Fornecedor manualmente passando os dados que deseja alterar.")
	public ResponseEntity<Fornecedor> updateFornecedor(@Valid @RequestBody Fornecedor fornecedor) {
		return new ResponseEntity<>(fornecedorService.updateFornecedor(fornecedor), HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar o endereço de um Fornecedor através da API Externa ViaCEP.")
	public ResponseEntity<Fornecedor> updateAddressFornecedor(@PathVariable Integer id, @RequestParam String cep) {
		return new ResponseEntity<>(fornecedorService.updateFornecedor(fornecedorService.updateAddressFornecedor(
				fornecedorService.findFornecedorById(id), fornecedorService.getCepDTOFromExternal(cep))), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Deletar um Fornecedor através de um ID único.")
	public ResponseEntity<String> deleteFornecedor(@PathVariable Integer id) {
		if (fornecedorService.findFornecedorById(id) == null) {
			return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
		} else {
			fornecedorService.deleteFornecedor(id);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
	}

}
