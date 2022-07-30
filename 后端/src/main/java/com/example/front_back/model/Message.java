package com.example.front_back.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {
    private String sender;
    private String message;
    private String sendTimeStamp;
    private String receiver;
    private Integer status = 0;//接受方接收成功

    public Message(String sender,String message,String sendTimeStamp,String receiver){
        this.sender = sender;
        this.message = message;
        this.sendTimeStamp = sendTimeStamp;
        this.receiver = receiver;
    }

    public Message(String message){
        this.sendTimeStamp = Long.toString(System.currentTimeMillis());
        this.message = message;
        this.sender = "System";
    }
}
