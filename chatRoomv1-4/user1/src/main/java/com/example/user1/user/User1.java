package com.example.user1.user;

import com.example.rabbitmq.config.RabbitConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class User1 {
    @RabbitListener(queues = RabbitConfig.QUEUE1)
    public void recvMsg(Message msg){
        System.out.println(new String(msg.getBody()));
    }
}
