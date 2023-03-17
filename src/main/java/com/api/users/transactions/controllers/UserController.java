package com.api.users.transactions.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.users.transactions.dtos.UserDto;
import com.api.users.transactions.enums.RoleName;
import com.api.users.transactions.models.RoleModel;
import com.api.users.transactions.models.UserModel;
import com.api.users.transactions.repositories.RoleRepository;
import com.api.users.transactions.services.UserService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	RoleRepository roleRepository;
	
	@PostMapping
	public ResponseEntity<Object> saveUser(@RequestBody @Valid UserDto userDto, BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Invalid user data");
	    }
		
		Optional<UserModel> username = userService.findByUsername(userDto.getUsername());
		Optional<UserModel> email = userService.findByEmail(userDto.getEmail());
		Optional<UserModel> cpf = userService.findByCpf(userDto.getCpf());
		
		if (username.isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Username already in use!");
		};
		
		if (email.isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Email already in use!");
		};
		
		if (cpf.isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: CPF/CNPJ already in use!");
		};
		
		if (!userDto.isValidCpfOrCnpj()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: CPF/CNPJ invalid!");
		};
		
		UserModel userModel = new UserModel();
		BeanUtils.copyProperties(userDto, userModel);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
		
		List<RoleModel> roles = new ArrayList<>();
        for (RoleName roleName : userDto.getRoles()) {
            RoleModel role = roleRepository.findByRoleName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(role);
        }
        userModel.setRoles(roles);
        
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userModel));
	};
	
	@GetMapping
	public ResponseEntity<List<UserModel>> getAllUsers(){
		return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
	};
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getOneUser(@PathVariable(value = "id") UUID id){
		Optional <UserModel> userOptional = userService.findById(id);
		if (!userOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
		};
		return ResponseEntity.status(HttpStatus.OK).body(userOptional.get());
	};
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") UUID id){
		Optional <UserModel> userOptional = userService.findById(id);
		if (!userOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User  not found.");
		};
		userService.delete(userOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body("User deleted successfuly.");
	};
	
	@PutMapping("/{id}")
	public ResponseEntity<Object> updateUser(@PathVariable(value = "id") UUID id, @RequestBody @Valid UserDto userDto, BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Invalid user data");
	    }
		
		Optional <UserModel> userOptional = userService.findById(id);
		if (!userOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
		};
		
		UserModel userModel = userOptional.get();
		BeanUtils.copyProperties(userDto, userModel, "id");
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
		userService.save(userModel);
		return ResponseEntity.status(HttpStatus.OK).body("User updated successfuly.");
	};
	
	
}
