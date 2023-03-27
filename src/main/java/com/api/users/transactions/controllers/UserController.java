package com.api.users.transactions.controllers;

import java.util.List;
import java.util.UUID;

import com.api.users.transactions.dtos.UserLoginDto;
import com.api.users.transactions.dtos.UserAuthResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.users.transactions.dtos.UserRegisterDto;
import com.api.users.transactions.models.UserModel;
import com.api.users.transactions.services.UserService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@PostMapping("/login")
	public ResponseEntity<UserAuthResponseDto> authenticate(@RequestBody UserLoginDto userLoginDto,BindingResult bindingResult) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.login(userLoginDto,bindingResult));
	}

	@PostMapping("/register")
	public ResponseEntity<Object> saveUser(@RequestBody @Valid UserRegisterDto userRegisterDto, BindingResult bindingResult){
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(userRegisterDto,bindingResult));
	};
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getUser(@PathVariable(value = "id") UUID id){
		return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id).get());
	};
	
	@GetMapping
	public ResponseEntity<List<UserModel>> getAllUsers(){
		return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
	};
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") UUID id){
		return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(id));
	};
	
	
}
