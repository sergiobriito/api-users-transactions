package com.api.users.transactions.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.api.users.transactions.dtos.TransactionDto;
import com.api.users.transactions.exception.ApiRequestException;
import com.api.users.transactions.models.TransactionModel;
import com.api.users.transactions.models.UserModel;
import com.api.users.transactions.configs.rabbitmq.RabbitMQService;
import com.api.users.transactions.repositories.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final UserService userService;
	private final RabbitMQService rabbitMQService;

	public TransactionService(TransactionRepository transactionRepository, UserService userService, RabbitMQService rabbitMQService) {
		this.transactionRepository = transactionRepository;
		this.userService = userService;
		this.rabbitMQService = rabbitMQService;
	}

	@Transactional
	public TransactionModel save(TransactionDto transactionDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
	        throw new ApiRequestException("Invalid transaction data");
	    }
		
		Optional<UserModel> payer = userService.findByUsername(transactionDto.getPayer());
		Optional<UserModel> payee = userService.findByUsername(transactionDto.getPayee());
		
		if (!payer.isPresent()) {
			throw new ApiRequestException("Payer username not found.");
	    };
	    if (!payee.isPresent()) {
			throw new ApiRequestException("Payee username not found.");
	    };
	    if (payer.get().getBalance().compareTo(transactionDto.getValue()) < 0) {
			throw new ApiRequestException("Payer does not have enough balance.");
	    };
	    
	    payer.get().setBalance(payer.get().getBalance().subtract(transactionDto.getValue()));
	    payee.get().setBalance(payee.get().getBalance().add(transactionDto.getValue()));
		TransactionModel transactionModel = new TransactionModel();
		BeanUtils.copyProperties(transactionDto, transactionModel); 
		transactionModel.setTransactionDate(LocalDateTime.now(ZoneId.of("UTC")));
		transactionRepository.save(transactionModel);
		
		String messageMQP = "{ \"transaction_id\": \"" + 
				transactionModel.getUuid() + "\", \"payer\": \"" + 
				transactionModel.getPayer() + "\", \"payee\": \"" + 
				transactionModel.getPayee() + "\", \"value\": " + 
				transactionModel.getValue() + " }";
	
		rabbitMQService.sendMessage(messageMQP);
		return transactionModel;	
	}

	public List<TransactionModel> findAll() {
		return transactionRepository.findAll();
	};

}
