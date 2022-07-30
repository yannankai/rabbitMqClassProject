package com.example.front_back;

import com.example.front_back.controller.ChatController;
import com.example.front_back.dao.MessageDao;
import com.example.front_back.dao.UserDao;
import com.example.front_back.mapper.MessageMapper;
import com.example.front_back.mapper.UserMapper;
import com.example.front_back.model.ChatWith;
import com.example.front_back.model.Message;
import com.example.front_back.model.User;
import com.example.front_back.utils.Constant;
import com.example.front_back.utils.Json2Obj;
import com.example.front_back.utils.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FrontBackApplicationTests {

    @Autowired
    UserDao userDao;

    @Autowired
    MessageDao messageDao;

    @Test
    void contextLoads() {
        User l = userDao.getUserById("viende");
        System.out.println(l.getUserName());
    }

    @Test
    void createTest(){
        userDao.createUser("viende2","123456");
    }

    @Test
    void AllUsersTest(){
        List<User> l =  userDao.getAllUsers();
        for(User i:l){
            System.out.println(i.getUserName());
        }
    }

    @Test
    void getAll(){
        List<User> users = userDao.getAllUsers();
        for(User user:users){
            System.out.println(user);
        }
        System.out.println("获取完毕");
    }

    @Test
    void getAllMessage(){
        System.out.println(messageDao.getAllMessageGroup());
    }

    @Test
    void testMessage(){
        List<Message>  ls = messageDao.getAllMessageGroup();
        for(Message m:ls){
            System.out.println(m.getMessage());
            System.out.println(m.getSender());
            System.out.println(m.getSendTimeStamp());
        }
    }

    @Test
    void testCDB(){
        ChatWith chatWith = new ChatWith("test1","test2");
        userDao.createChatDB(chatWith);
    }

    @Test
    void testChat(){
        ChatWith chatWith = new ChatWith("test1","test2");
        Message message = new Message("1","er ge",new Date().toString(),"viende");
        messageDao.insertSingleChatMessage(message,chatWith);
    }

    @Test
    void asdas(){
        String str = "\"{\\\"sender\\\":\\\"lyl\\\",\\\"receiver\\\":\\\"viende\\\",\\\"message\\\":\\\"刘云龙也牛逼\\\",\\\"sendTimeStamp\\\":\\\"1648125275977\\\"}\"";
        Message message = (Message) Json2Obj.json2OBJ(str,Message.class);
        System.out.println(Json2Obj.obj2Json(message));
    }

    @Autowired
    ChatController chatController;
    @Test
    void tr(){
        String text = chatController.translateEnglishToChinese("I want to sleep");
        System.out.println(text);
    }


}
