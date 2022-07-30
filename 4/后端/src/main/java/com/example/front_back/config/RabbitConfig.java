package com.example.front_back.config;

import com.example.front_back.dao.MessageDao;
import com.example.front_back.mapper.MessageSendLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public final static Logger logger =  LoggerFactory.getLogger(RabbitConfig.class);
    @Autowired
    CachingConnectionFactory cachingConnectionFactory;
    @Autowired
    MessageSendLogMapper messageSendLogMapper;

    @Bean
    RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
            //如果到了exchange，会被confirm
        rabbitTemplate.setConfirmCallback((data,ack,cause)->{
            String msgCorrelationId = data.getId();
            if (ack){ //data:correlationData(在发送的时候会设置ID，我们也只需要id（设为了时间戳）)  ack:是否确认   cause:出错原因
                //如果被确认（发送成功）
                System.out.println("消息发送成功到exchange");
                logger.info(msgCorrelationId+"消息成功到达exchange");
                //改变发送消息的status
                messageSendLogMapper.renewMessageLogStatus(msgCorrelationId,1);
            }else{
                logger.info(msgCorrelationId+"消息发送失败,未到exchange");
                messageSendLogMapper.renewMessageLogStatus(msgCorrelationId,2);
            }
        });
        //发送没有被confirm（没有到达queue）
        rabbitTemplate.setReturnsCallback((returnedMessage)->{
            System.out.println("消息没有成功到达queue,丢失了");
            logger.warn("消息丢失，未到达queue，请检查是否由queue和exchange绑定");
        });
        return rabbitTemplate;
    }

//    @Override //在rabbitmqTemplate中没有生效，试试这种方式
//    public void returnedMessage(ReturnedMessage returnedMessage) {
//        System.out.println("消息没有成功到达queue,丢失了");
//        logger.warn("消息丢失，未到达queue，请检查是否由queue和exchange绑定");
//    }

    public final static String EXCHANGE_TOPIC = "chat";
    public final static String SINGLE_CHAT_QUEUE_NAME = "single_store_in_db";
    public final static String GROUP_CHAT_QUEUE_NAME = "group_store_in_db";
    @Bean
    TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE_TOPIC,false,false);
    }

    @Bean
    Queue singleQueue(){
        return new Queue(SINGLE_CHAT_QUEUE_NAME,false,false,false);
    }

    @Bean
    Queue groupQueue(){
        return new Queue(GROUP_CHAT_QUEUE_NAME,false,false,false);
    }

    @Bean
    Binding createSingleBind(){
        return BindingBuilder.bind(singleQueue())
                        .to(topicExchange()).with("single.#");
    }
    @Bean
    Binding createGroupBind(){
        return BindingBuilder.bind(groupQueue())
                .to(topicExchange()).with("group.#");
    }

}
