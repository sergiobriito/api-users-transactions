package com.api.users.transactions.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.api.users.transactions.models.UserModel;
import com.api.users.transactions.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserModel userModel = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
		return new User(userModel.getUsername(), userModel.getPassword(),true,true,true,true, userModel.getAuthorities());
	}
	
	@Transactional
	public UserModel save(UserModel userModel) {
		return userRepository.save(userModel);	
	};

	public List<UserModel> findAll() {
		return userRepository.findAll();
	}

	public Optional<UserModel> findById(UUID id) {
		return userRepository.findById(id);
	}
	
	@Transactional
	public void delete(UserModel userModel) {
		userRepository.delete(userModel);	
	}

	public Optional<UserModel> findByEmail(String email) {
		return userRepository.findByEmail(email);
	};

	public Optional<UserModel> findByCpf(String cpf) {
		return userRepository.findByCpf(cpf);
	};
	
	public Optional<UserModel> findByUsername(String username) {
		return userRepository.findByUsername(username);
	};
	
}
