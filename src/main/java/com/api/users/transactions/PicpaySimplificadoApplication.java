package com.api.users.transactions;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class PicpaySimplificadoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PicpaySimplificadoApplication.class, args);
	}

}
