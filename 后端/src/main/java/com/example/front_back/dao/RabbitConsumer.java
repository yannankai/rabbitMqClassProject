package com.example.front_back.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.front_back.config.RabbitConfig;
import com.example.front_back.controller.WebSocketServer;
import com.example.front_back.model.ChatWith;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;


@Component
public class RabbitConsumer {
    @Autowired
    private MessageDao messageDao;

    @RabbitListener(queues = RabbitConfig.SINGLE_CHAT_QUEUE_NAME)
    public void recvSingleMsg(Message AMQPSendMessage , Channel channel){
        long deliveryTag = AMQPSendMessage.getMessageProperties().getDeliveryTag();
        com.example.front_back.model.Message message = JSONObject.parseObject(new String(AMQPSendMessage.getBody())).toJavaObject(com.example.front_back.model.Message.class);
        System.out.println("单聊收到消息，开始存消息为:");
        System.out.println(message);
        try {
          ChatWith chatWith = new ChatWith(message.getSender(),message.getReceiver());chatWith.sort();
          int insertNum = messageDao.insertSingleChatMessage(message,chatWith);
          if (insertNum==1) System.out.println("RabbitMQ插入单聊消息成功");
          channel.basicAck(deliveryTag,false);//确定消息的标记（标识），第二个参数：仅仅确认当前消息（true:之前所有的消息全都成功）
            messageDao.updateStatusCode(message,1);
        }catch (Exception e){
         try {
             channel.basicNack(deliveryTag,false,true);//告诉MQ 消费失败 第三个参数表示是否重新入队，否则进入死信队列
             messageDao.updateStatusCode(message,2); //发送失败，状态为2
             e.printStackTrace();
         }catch (Exception err){
             err.printStackTrace();
         }
      }
    }

    @RabbitListener(queues = RabbitConfig.GROUP_CHAT_QUEUE_NAME)
    public void recvGroupMsg(Message message,Channel channel){
        System.out.println("群聊收到消息，开始存");
        String json = new String(message.getBody());
        long deliverTag = message.getMessageProperties().getDeliveryTag();
        com.example.front_back.model.Message msg = JSONObject.parseObject(new String(message.getBody())).toJavaObject(com.example.front_back.model.Message.class);
        try {
            int insertNum = messageDao.insertMessage(msg);
            if (insertNum==1) System.out.println("RabbitMQ插入群聊消息成功");
            channel.basicAck(deliverTag,false);
            messageDao.updateGroupMessageStatusCode(1,msg.getSendTimeStamp());
        }catch (Exception e){
            try {
                channel.basicNack(deliverTag,false,true);
                messageDao.updateGroupMessageStatusCode(2,msg.getSendTimeStamp());
            }catch (Exception err){
                err.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = RabbitConfig.DEADQUEUE)
    @SneakyThrows
    public void handleDelayMsg(Message message){
        String series = new String(message.getBody());
        JSONObject job = JSON.parseObject(series);
        com.example.front_back.model.Message msg = job.toJavaObject(com.example.front_back.model.Message.class);
        //从死信队列拿到msg，然后发送给DB和webserver
        String receiver = msg.getReceiver();
        ConcurrentHashMap<String, WebSocketServer> webSocketSet= WebSocketServer.getWebSocketSet();
        String senderID = msg.getSender();
        WebSocketServer sender = webSocketSet.get(senderID);
        if (receiver.equals("group"))
            sender.sendtoAll(series);
        else
            sender.sendtoUser(series,receiver);
    }

}
