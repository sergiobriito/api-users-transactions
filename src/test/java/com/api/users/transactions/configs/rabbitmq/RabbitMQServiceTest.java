package com.api.users.transactions.configs.rabbitmq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.amqp.core.Queue;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
class RabbitMQServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private Queue queue;

    @InjectMocks
    private RabbitMQService rabbitMQService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendMessageToRabbitMQ() {
        String message = "Testing RabbitMQ!";
        rabbitMQService.sendMessage(message);
        verify(rabbitTemplate).convertAndSend(queue.getName(), message);

    }
}