package com.api.users.transactions.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.api.users.transactions.dtos.TransactionDto;
import com.api.users.transactions.models.TransactionModel;
import com.api.users.transactions.models.UserModel;
import com.api.users.transactions.services.RabbitMQService;
import com.api.users.transactions.services.TransactionService;
import com.api.users.transactions.services.UserService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/transaction")
public class TransactionController {
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	UserService userService;
	
	@Value("${external.service.auth.transaction.url}")
	private String externalServiceUrl;
	
	@Value("${external.service.notification.transaction.url}")
	private String externalNotificationServiceUrl;
	
	@Autowired
	RabbitMQService rabbitMQService;
	
	@PostMapping
	@Transactional
	public ResponseEntity<Object> saveTransaction(@RequestBody @Valid TransactionDto transactionDto, BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Invalid transaction data");
	    }
		
		Optional<UserModel> payer = userService.findByUsername(transactionDto.getPayer());
		Optional<UserModel> payee = userService.findByUsername(transactionDto.getPayee());
		
		if (!payer.isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Payer username not found.");
	    };
	    if (!payee.isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Payee username not found.");
	    };
	    if (payer.get().getBalance().compareTo(transactionDto.getValue()) < 0) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Payer does not have enough balance.");
	    };
	    
	    try {
	    	RestTemplate restTemplate = new RestTemplate();
		    String mockResponse = restTemplate.getForObject(externalServiceUrl, String.class);
		    JSONObject jsonObject = new JSONObject(mockResponse);
		    String message = jsonObject.getString("message");
		    if(!message.equals("Autorizado")) {
		    	return ResponseEntity.status(HttpStatus.CONFLICT).body("External service: " + mockResponse);
		    }; 
	    }catch(Exception e) {
	    	return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: External service error.");
	    };
	    
	    
	    payer.get().setBalance(payer.get().getBalance().subtract(transactionDto.getValue()));
	    payee.get().setBalance(payee.get().getBalance().add(transactionDto.getValue()));
		TransactionModel transactionModel = new TransactionModel();
		BeanUtils.copyProperties(transactionDto, transactionModel); 
		transactionModel.setTransactionDate(LocalDateTime.now(ZoneId.of("UTC")));
		transactionService.save(transactionModel);
		
		String messageMQP = "{ \"transaction_id\": \"" + transactionModel.getUuid() + "\", \"payer\": \"" + transactionModel.getPayer() + "\", \"payee\": \"" + transactionModel.getPayee() + "\", \"value\": " + transactionModel.getValue() + " }";
		rabbitMQService.sendMessage(messageMQP);
		
		try {
	    	RestTemplate restTemplate = new RestTemplate();
		    String mockResponse = restTemplate.getForObject(externalNotificationServiceUrl, String.class);
		    JSONObject jsonObject = new JSONObject(mockResponse);
		    String message = jsonObject.getString("message");
		    if(!message.equals("Success")) {
		    	JSONObject errorJson = new JSONObject();
		        errorJson.put("transaction", "Success");
		        errorJson.put("notification", "Error");
		    	return ResponseEntity.status(HttpStatus.CREATED).body(errorJson.toString());
		    };
		    JSONObject successJson  = new JSONObject();
		    successJson .put("transaction", "Success");
		    successJson .put("notification", "Success");
		    return ResponseEntity.status(HttpStatus.CREATED).body(successJson.toString());
	    }catch(Exception e) {
	    	JSONObject errorJson = new JSONObject();
	        errorJson.put("transaction", "Success");
	        errorJson.put("notification", "Error");
	    	return ResponseEntity.status(HttpStatus.CREATED).body(errorJson.toString());
	    }
			
	};
	
	@GetMapping
	public ResponseEntity<List<TransactionModel>> getAllTransactions(){
		return ResponseEntity.status(HttpStatus.OK).body(transactionService.findAll());
	};

}
