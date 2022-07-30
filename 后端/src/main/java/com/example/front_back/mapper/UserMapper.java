package com.example.front_back.mapper;

import com.example.front_back.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper {
    User getUserById(@Param("username")String username);
    Integer createUser(@Param("username")String username,@Param("password")String password);
    List<User> getAllUsers();
    String test();



    @Update({"create table " +
            "${tableName}(  " +
            "sender varchar(255)," +
            "status int," +
            " receiver varchar(255) ," +
            " send_time_stamp varchar(255),"+
            " message  varchar(10000) ,primary key(send_time_stamp) );"
           })
    Integer createChatDB(@Param("tableName") String tableName);
}
