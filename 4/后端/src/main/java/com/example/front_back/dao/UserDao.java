package com.example.front_back.dao;

import com.example.front_back.mapper.UserMapper;
import com.example.front_back.model.ChatWith;
import com.example.front_back.model.Message;
import com.example.front_back.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao {

    @Autowired
    UserMapper userMapper;

    public User getUserById(String username){
        return userMapper.getUserById(username);
    }
    public Integer createUser(String username,String password){
        return userMapper.createUser(username,password);
    }
    public List<User> getAllUsers(){
        return userMapper.getAllUsers();
    }
    public String test(){
        return userMapper.test();
    }


    public Integer createChatDB(ChatWith chatWith){
        return userMapper.createChatDB(chatWith.getFrom()+"_"+chatWith.getTo());
    }



}
