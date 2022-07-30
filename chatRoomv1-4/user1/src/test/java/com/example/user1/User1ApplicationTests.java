package com.example.user1;

import com.example.rabbitmq.config.RabbitConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class User1ApplicationTests {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    void contextLoads() {
        Message message = MessageBuilder.withBody("{\"msg\":\"good news\",\"receiver\":\"user2\",\"sender\":\"user1\"}".getBytes())
                .build();
        rabbitTemplate.send(RabbitConfig.EXCHANGE_NAME,RabbitConfig.QUEUE2,message);
    }

}
