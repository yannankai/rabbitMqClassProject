package com.example.front_back.dao;

import com.example.front_back.mapper.MessageMapper;
import com.example.front_back.model.ChatWith;
import com.example.front_back.model.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class MessageDao {
    @Autowired
    MessageMapper messageMapper;

    public Integer insertMessage(Message message){
      return   messageMapper.insertMessage(message);
    }
    public List<Message> getDateMessage(Date begin, Date end, String sender, String receiver){
       return  messageMapper.getDateMessage(begin,end,sender,receiver);
    }
    public List<Message> getAllMessageGroup(){
        List<Message> list =  messageMapper.getAllMessageGroup();
        for(Message msg:list){
            msg.setSendTimeStamp((new Date(Long.parseLong(msg.getSendTimeStamp()))).toString());
        }
        return list;
    }

    public List<Message> getSingleChatInfo(ChatWith chatWith){
        List<Message> list = messageMapper.getSingleChatInfo(chatWith.getFrom()+"_"+chatWith.getTo());
        for(Message msg:list){
            msg.setSendTimeStamp((new Date(Long.parseLong(msg.getSendTimeStamp()))).toString());
        }
        return list;
    }

    public Integer insertSingleChatMessage(Message message,ChatWith chatWith){
        String tableName = chatWith.getFrom()+"_"+chatWith.getTo();
        System.out.println(tableName);
        String sender = message.getSender();
        String msg = message.getMessage();
        String sendTimeStamp = message.getSendTimeStamp();
        String receiver = message.getReceiver();
        return messageMapper.insertSingleChatMessage(tableName,sender,msg,sendTimeStamp,receiver);
    }

    public Integer updateStatusCode(Message message,Integer status){
        ChatWith chatWith = new ChatWith(message.getSender(),message.getReceiver());
        chatWith.sort();
        String sendTimeStamp = message.getSendTimeStamp();
        int res = 0;
        if (message.getReceiver()=="group"){
            String tableName = "user_message";
             res = messageMapper.updateStatusCode(tableName,status,sendTimeStamp);
            System.out.println(res+"是返回值,renew ok,timestamp:"+sendTimeStamp);
        }else {
            String tableName = chatWith.getFrom()+"_"+chatWith.getTo();
            res = messageMapper.updateStatusCode(tableName,status,sendTimeStamp);
            System.out.println(res+"是返回值,renew ok,timestamp:"+sendTimeStamp);
        }

        return res;
    }

    public Integer updateGroupMessageStatusCode(Integer status,String sendTimeStamp){
        return messageMapper.updateGroupMessageStatusCode(status,sendTimeStamp);
    }

}
