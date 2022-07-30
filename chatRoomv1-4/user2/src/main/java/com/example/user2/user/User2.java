package com.example.user2.user;

import com.alibaba.fastjson.JSON;
import com.example.rabbitmq.config.RabbitConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class User2 {
    @RabbitListener(queues = RabbitConfig.QUEUE2)
    public void recvMsg(Message msg){
        String json = new String(msg.getBody());
        MyMessage message = JSON.parseObject(json).toJavaObject(MyMessage.class);
        System.out.println(message.getMsg());
    }
}
