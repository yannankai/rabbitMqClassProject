package com.example.front_back.mapper;

import com.example.front_back.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface MessageMapper {
    Integer insertMessage(Message message);
    List<Message> getDateMessage(Date begin,Date end,String sender,String receiver); //receiver 0  是群发
    List<Message> getAllMessageGroup();

    @Insert("insert ${tableName}(sender,message,send_time_stamp,receiver) values(#{sender},#{message},#{sendTimeStamp},#{receiver})")
    Integer insertSingleChatMessage(String tableName,String sender,String message,String sendTimeStamp,String receiver);
    @Select({"select * from ${tableName} where status = 1;"})
    List<Message> getSingleChatInfo(@Param("tableName") String tableName);

    @Update("update ${tableName} set status = ${status} where send_time_stamp=#{sendTimeStamp};")
    Integer updateStatusCode(@Param("tableName")String tableName,Integer status,String sendTimeStamp);
    @Update("update user_message set status = ${status} where send_time_stamp=#{sendTimeStamp};")
    Integer updateGroupMessageStatusCode(Integer status,String sendTimeStamp);

}
