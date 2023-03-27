package com.api.users.transactions;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class UsersTransactions {
	public static void main(String[] args) {
		SpringApplication.run(UsersTransactions.class, args);
	}

}
