package com.example.front_back.mapper;

import com.example.front_back.model.MessageSendLog;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageSendLogMapper {
//表的名字：user1_user2_send_log
    @Update({"create table " +
            "send_log(    " +
            " msg_id varchar(255) ," +
            " status int,"+
            " count int ," +
            " receiver varchar(255) ," +
            " sender varchar(255) ," +
            " tryTime varchar(255) ," +
            " primary key(msg_id) );"
    })
    Integer createSendLogTable();

    @Update("update send_log set status = #{status} where msg_id = #{msgId};")
    Integer renewMessageLogStatus(String msgId,Integer status);

    @Update("update send_log set count = count+1 where msg_id = #{msgId} tryTime = #{tryTime};")
    Integer renewMessageLogCount(String tryTime, String msgId);

    @Insert("insert send_log" +
            "(msg_id,status,exchange,count,receiver,sender,tryTime) " +
            "values(#{msgId},#{status},#{exchange},#{count},#{receiver},#{sender});"
    )
    Integer insertSendMsgLog(String msgId,String sender,String receiver,Integer status,Integer count);

    @Delete("delete from send_log;")
    Integer delLogData();//每日清理

    //获取所有需要处理的message
    @Select("select * from send_log where status = 0 and tryTime &lt; sysdate();")
    List<MessageSendLog> getAllMessageNeededHandle();

}
