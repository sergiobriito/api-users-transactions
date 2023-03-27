package com.api.users.transactions.controllers;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.users.transactions.dtos.TransactionDto;
import com.api.users.transactions.models.TransactionModel;
import com.api.users.transactions.services.TransactionService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@RequestMapping("api/v1/transactions")
public class TransactionController {
	private final TransactionService transactionService;

	@PostMapping
	@Transactional
	public ResponseEntity<Object> saveTransaction(@RequestBody @Valid TransactionDto transactionDto, BindingResult bindingResult){
		return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.save(transactionDto,bindingResult));
			
	};
	
	@GetMapping
	public ResponseEntity<List<TransactionModel>> getAllTransactions(){
		return ResponseEntity.status(HttpStatus.OK).body(transactionService.findAll());
	};

}
