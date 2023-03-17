package com.api.users.transactions.services;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {

	@Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    public void sendMessage(String message) {
    	rabbitTemplate.convertAndSend(this.queue.getName(), message);
    }
}
