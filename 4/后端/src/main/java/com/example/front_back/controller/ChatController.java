package com.example.front_back.controller;

import com.example.front_back.dao.MessageDao;
import com.example.front_back.dao.UserDao;
import com.example.front_back.model.Message;
import com.example.front_back.model.User;
import com.example.front_back.utils.Constant;
import com.example.front_back.utils.Response;
import com.example.front_back.utils.TranslateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ChatController {

    @Autowired
    UserDao userDao;

    @Autowired
    MessageDao messageDao;

    @PostMapping("/send/group")
    public Message sendGroupMsg(@RequestBody Map<String,String> map){
        Message msg = new Message(map.get("sender"),map.get("message"),Long.toString(System.currentTimeMillis()),map.get("receiver"));
        System.out.println(messageDao.insertMessage(msg)); //返回值：插入的条数
        System.out.println(map.get("sender")+"群发了:"+map.get("message"));

        return msg;
    }

    @GetMapping("/group/message")
    public List<Message> getAllMessage(){
        List<Message> list = messageDao.getAllMessageGroup();
        System.out.println(list.get(0).getSendTimeStamp());
        return list;
    }

    @PostMapping("/translate/en2zh")
    @SneakyThrows
    public String translateEnglishToChinese(@RequestBody String english){
        try {
            String res = TranslateUtil.translateEnToZh(english);
            return res;
        } catch (JsonProcessingException e) {
            String res = "翻译错误";
            e.printStackTrace();
            return res;
        }
    }

    @GetMapping("/all/Users")
    public List<User> getAllUsers(){
        System.out.println("现在开始获取成员列表");
        List<User> users = userDao.getAllUsers();
        System.out.println("获取所有成员列表成功");
        return users;
    }


    @PostMapping("/login")
    public Response login(@RequestBody User user){
        System.out.println(user);
        User res = userDao.getUserById(user.getUserName());
        if (res==null){
            System.out.println("需要注冊");
            return new Response(Constant.NEEDED_REGISTERED,"需要注冊");
        }else{
            if (res.getPassword().equals(user.getPassword())){
                System.out.println("登陆成功");
                return new Response(Constant.SUCCESS,"登陆成功");
            }else{
                System.out.println("密码错误");
                return new Response(Constant.PASSWORD_ERROR,"密码错误");
            }
        }
    }

    @PostMapping("/register")
    public Response register(@RequestBody User user){
        User res = userDao.getUserById(user.getUserName());
        if (res!=null){
            return new Response(Constant.REGISTERED,"用戶已被注冊");
        }else{
            int status = userDao.createUser(user.getUserName(),user.getPassword());
            if (status==1){
                return new Response(Constant.SUCCESS,"注冊成功");
            }else{
                return new Response(Constant.ERROR,"注冊失敗，請重試");
            }
        }
    }

    @GetMapping("/test")
    public String test(){
        return userDao.test();
    }

}
