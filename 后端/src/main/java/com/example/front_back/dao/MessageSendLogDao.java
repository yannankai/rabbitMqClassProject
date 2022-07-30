package com.example.front_back.dao;

import com.example.front_back.mapper.MessageSendLogMapper;
import com.example.front_back.model.ChatWith;
import com.example.front_back.model.MessageSendLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageSendLogDao {

    @Autowired
    MessageSendLogMapper messageSendLogMapper;

    public Integer createSendLogTable(){//前面需要try-catch一下，如果已经有了DB，那就无需创建
        //每天都要删除DB，有未发送的消息log，第二天也会删除
        return messageSendLogMapper.createSendLogTable();
    }

    public Integer delLogData(){
        return messageSendLogMapper.delLogData();
    }

    public Integer insertSendMsgLog(MessageSendLog messageSendLog){
        return messageSendLogMapper.insertSendMsgLog(messageSendLog.getMsgId(),messageSendLog.getSender(),messageSendLog.getReceiver(),messageSendLog.getStatus(),messageSendLog.getCount());
    }

    public Integer renewMessageLogStatus(String msgId,Integer status){
        return messageSendLogMapper.renewMessageLogStatus(msgId,status);
    }

    public List<MessageSendLog> getAllMessageNeededHandle(){
        return messageSendLogMapper.getAllMessageNeededHandle();
    }


}
