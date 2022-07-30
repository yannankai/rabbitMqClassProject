package com.example.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE_NAME = "exchange_name";
    public static final String QUEUE1 = "queue1";
    public static final String QUEUE2 = "queue2";
    public static final String QUEUE3 = "queue3";

    @Bean
    Binding createBind1(){
        return BindingBuilder.bind(createQ1()).to(createExchange());
    }
    @Bean
    Binding createBind2(){
        return BindingBuilder.bind(createQ2()).to(createExchange());
    }
    @Bean
    Binding createBind3(){
        return BindingBuilder.bind(createQ3()).to(createExchange());
    }

    @Bean
    FanoutExchange createExchange(){
        return new FanoutExchange(EXCHANGE_NAME,true,false);
    }

    @Bean
    Queue createQ1(){
        return new Queue(QUEUE1,true,false,false);
    }
    @Bean
    Queue createQ2(){
        return new Queue(QUEUE2,true,false,false);
    }
    @Bean
    Queue createQ3(){
        return new Queue(QUEUE3,true,false,false);
    }


}
