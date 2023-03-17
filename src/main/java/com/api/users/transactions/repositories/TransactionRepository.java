package com.api.users.transactions.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.users.transactions.models.TransactionModel;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionModel, UUID>{
	

}
