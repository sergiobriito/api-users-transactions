package com.api.users.transactions.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.users.transactions.models.TransactionModel;
import com.api.users.transactions.repositories.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {
	
	@Autowired
	TransactionRepository transactionRepository;

	@Transactional
	public TransactionModel save(TransactionModel transactionModel) {
		return transactionRepository.save(transactionModel);	
	}

	public List<TransactionModel> findAll() {
		return transactionRepository.findAll();
	};

}
