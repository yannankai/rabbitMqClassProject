package com.example.front_back.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.front_back.config.RabbitConfig;
import com.example.front_back.dao.MessageDao;
import com.example.front_back.dao.UserDao;
import com.example.front_back.mapper.MessageMapper;
import com.example.front_back.mapper.UserMapper;
import com.example.front_back.model.ChatWith;
import com.example.front_back.model.Message;
import com.example.front_back.utils.Constant;
import com.example.front_back.utils.Response;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/SingleChat")
public class SingleChatDB {

    @Autowired
    private UserDao userDao;

    @Autowired
    private MessageDao messageDao;

    SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd");

    @GetMapping("/getAllSingleChatInfo/{chater1}/{chater2}")
    public List<Message> getAllSingleChatInfo(@PathVariable("chater1") String chater1,@PathVariable("chater2") String chater2){
        ChatWith chatWith  =  new ChatWith(chater1,chater2);
        chatWith.sort();
        return messageDao.getSingleChatInfo(chatWith);
    }

    @PostMapping("/send/pdf")
    public Map<String,Object> fileUpLoad(MultipartFile file, HttpServletRequest req){
        Map<String,Object> result = new HashMap<>();
        String originName = file.getOriginalFilename();
        if (!originName.endsWith(".pdf")){
            result.put("status","error");
            result.put("msg","文件类型不对");
            return result;
        }
        String format  = sdf.format(new Date());
        String realPath = req.getServletContext().getRealPath("/")+format;
        File folder = new File(realPath); //文件夹
        if (!folder.exists()){
            folder.mkdirs();
        }
        String newName = UUID.randomUUID().toString() + ".pdf"; //文件名
        try {
            file.transferTo(new File(folder,newName));
            String url = req.getScheme() + "://" + req.getServerName()+":"+req.getServerPort()+format+newName;
            result.put("status","success");
            result.put("url",url);
        } catch (IOException e) {
            result.put("status","error");
            result.put("msg",e.getMessage());
        }
        return result;

    }
    @Autowired
    RabbitTemplate rabbitTemplate;

    @PostMapping("/delay/message/")
    public void sendDelayMsg(@RequestBody String req){
        JSONObject jsonObject = JSONObject.parseObject(req);
        String innerMsg = jsonObject.get("msg").toString();
        String ms =  jsonObject.get("delayMs").toString();
        System.out.println(innerMsg);
        Message msg = JSONObject.parseObject(innerMsg).toJavaObject(Message.class);
        //把消息封装好成AMQP协议的message，发给broker
        org.springframework.amqp.core.Message msgSent = MessageBuilder.withBody(innerMsg.getBytes()).setExpiration(ms).build();
//        //broker发到一个无consumer的队列，设置好expire-time 然后发给死信队列
        rabbitTemplate.send(RabbitConfig.CLOCKExchange,RabbitConfig.CLOCKExchange,msgSent,new CorrelationData(msg.getSendTimeStamp()));
        System.out.println("发送完毕");
    }

    @PostMapping("/createDB")
    public Response createDB(@RequestBody ChatWith chatWith){
        //chatwith 按照字母序排序，创建DB
       chatWith.sort();
        try {
            Integer res = userDao.createChatDB(chatWith);
            System.out.println("DB聊天室成立OK");
            return new Response(Constant.SUCCESS,"创建DB成功");
        }catch (Exception e){
            //创建DB
            System.out.println("DB聊天室已然成立");
//            getAllSingleChatInfo(chatWith);
            return new Response(Constant.DB_EXISTS_ALREADY,"DB已存在");
        }
    }

//    @GetMapping("/getInfo/{page}")
//    public List<Message> getSingleChatMessage(@RequestBody ChatWith chatWith,@PathVariable("page") int page){
//
//    }




    @PostMapping("/sendInfo")
    public List<Message> sendSingleChatMessage(@RequestBody Message message){
        ChatWith chatWith = new ChatWith(message.getSender(),message.getReceiver());chatWith.sort();
        int res = messageDao.insertSingleChatMessage(message,chatWith);
        if (res==1){
            System.out.println("插入完成，返回现在二人的聊天记录");
            return messageDao.getSingleChatInfo(chatWith);
        }else{
            System.out.println("插入失败，请重新插入");
            return messageDao.getSingleChatInfo(chatWith);
        }


    }

}
