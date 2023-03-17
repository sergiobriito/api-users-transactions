package com.api.users.transactions.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.users.transactions.models.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID>{

	Optional<UserModel> findByUsername(String username);

	Optional<UserModel> findById(UUID id);

	Optional<UserModel> findByEmail(String email);

	Optional<UserModel> findByCpf(String cpf);
}
