package com.example.front_back.model;

import lombok.Data;

@Data
public class MessageSendLog {
    //发送方得知是否发送成功(不确定接收成功)    （是否接受成功在message里面的status改变）
    String msgId;
    Integer status;//0:未接受  1：已接受 2：多次失败，废弃
    String sender;
    String receiver;
    Integer count;//重发次数，等于3就over
    String tryTime;//上次的重发的时间
}
