package com.example.user1;

import com.example.rabbitmq.config.RabbitConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class Controller {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/send")
    public void sendMsg(@RequestBody String message){ //接受的Message是Messaging包的，发送的是amqp协议里的Message
        //暂时只支持文本
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME,null,message);
    }

}
