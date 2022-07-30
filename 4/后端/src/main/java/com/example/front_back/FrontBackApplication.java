package com.example.front_back;

import com.example.front_back.mapper.MessageSendLogMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FrontBackApplication {
    @Autowired
    private static MessageSendLogMapper messageSendLogMapper;

    public static void main(String[] args) {

        SpringApplication.run(FrontBackApplication.class, args);
    }

}
