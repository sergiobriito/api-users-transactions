package com.api.users.transactions.configs.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {
    private final RabbitTemplate rabbitTemplate;
    private final Queue queue;
    

    public RabbitMQService(RabbitTemplate rabbitTemplate, Queue queue) {
		super();
		this.rabbitTemplate = rabbitTemplate;
		this.queue = queue;
	}

	public void sendMessage(String message) {
    	rabbitTemplate.convertAndSend(this.queue.getName(), message);
    }

}
