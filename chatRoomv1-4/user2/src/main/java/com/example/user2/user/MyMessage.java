package com.example.user2.user;

import jdk.jfr.DataAmount;
import lombok.Data;

@Data
public class MyMessage {
    private String sender;
    private String receiver;
    private String msg;
}
