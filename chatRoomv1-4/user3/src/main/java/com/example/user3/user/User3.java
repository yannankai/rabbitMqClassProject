package com.example.user3.user;

import com.example.rabbitmq.config.RabbitConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class User3 {
    @RabbitListener(queues = RabbitConfig.QUEUE3)
    public void recvMsg(Message msg){
        System.out.println(msg.getBody());
    }
}
