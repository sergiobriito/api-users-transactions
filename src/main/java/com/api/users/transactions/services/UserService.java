package com.api.users.transactions.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.api.users.transactions.configs.security.JwtService;
import com.api.users.transactions.dtos.UserAuthResponseDto;
import com.api.users.transactions.dtos.UserLoginDto;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.api.users.transactions.dtos.UserRegisterDto;
import com.api.users.transactions.exception.ApiRequestException;
import com.api.users.transactions.models.RoleModel;
import com.api.users.transactions.models.UserModel;
import com.api.users.transactions.repositories.RoleRepository;
import com.api.users.transactions.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService  {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public UserService(UserRepository userRepository,
					   RoleRepository roleRepository,
					   JwtService jwtService,
					   AuthenticationManager authenticationManager) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}

	@Transactional
	public UserAuthResponseDto saveUser(UserRegisterDto userRegisterDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
	        throw new ApiRequestException("Invalid user data");
	    }
		
		Optional<UserModel> username = userRepository.findByUsername(userRegisterDto.getUsername());
		Optional<UserModel> email = userRepository.findByEmail(userRegisterDto.getEmail());
		Optional<UserModel> cpf = userRepository.findByCpf(userRegisterDto.getCpf());
		
		if (username.isPresent()) {
			throw new ApiRequestException("Username already in use!");
		};
		
		if (email.isPresent()) {
			throw new ApiRequestException("Email already in use!");
		};
		
		if (cpf.isPresent()) {
			throw new ApiRequestException("CPF/CNPJ already in use!");
		};
		
		if (!userRegisterDto.isValidCpfOrCnpj()) {
			throw new ApiRequestException("CPF/CNPJ invalid!");
		};
		
		UserModel userModel = new UserModel();
		BeanUtils.copyProperties(userRegisterDto, userModel);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
		
		List<RoleModel> roles = userRegisterDto
				.getRoles()
				.stream()
				.map(roleName -> roleRepository
						.findByRoleName(roleName)
						.orElseThrow(() -> new RuntimeException("Role not found")))
				.collect(Collectors.toList());

        userModel.setRoles(roles);
        userRepository.save(userModel);

		var jwtToken = jwtService.generateToken(userModel);
		return UserAuthResponseDto.builder()
				.token(jwtToken)
				.build();
	};

	public UserAuthResponseDto login(UserLoginDto userLoginDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ApiRequestException("Invalid user data");
		}

		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						userLoginDto.getUsername(),
						userLoginDto.getPassword()));

		var user = userRepository.findByUsername(userLoginDto.getUsername())
				.orElseThrow(() -> new ApiRequestException("User not found."));
		var jwtToken = jwtService.generateToken(user);

		return UserAuthResponseDto.builder()
				.token(jwtToken)
				.build();
	}

	@Transactional
	public String deleteUser(UUID id) {
		Optional <UserModel> userOptional = userRepository.findById(id);
		if (!userOptional.isPresent()) {
			throw new ApiRequestException("User not found.");
		};
		userRepository.delete(userOptional.get());

		return "User deleted successfuly.";
	}
	
	public List<UserModel> findAll() {
		return userRepository.findAll();
	}

	public Optional<UserModel> findById(UUID id) {
		Optional<UserModel> user = userRepository.findById(id);
		if (!user.isPresent()) {
			throw new ApiRequestException("User not found with id: " + id);
		};
		return userRepository.findById(id);
	}

	public Optional<UserModel> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	
	
}
