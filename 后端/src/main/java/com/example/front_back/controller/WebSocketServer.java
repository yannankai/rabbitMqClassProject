package com.example.front_back.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.front_back.config.RabbitConfig;
import com.example.front_back.dao.RabbitConsumer;
import com.example.front_back.mapper.MessageSendLogMapper;
import com.example.front_back.model.ChatWith;
import com.example.front_back.model.Message;
import com.example.front_back.utils.Json2Obj;
import com.example.front_back.utils.SpringContext;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.Time;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

//访问服务端的url地址
@Component
@ServerEndpoint(value = "/websocket/{id}") //记录当前用户的id（就是username）
public  class WebSocketServer {


    public SingleChatDB getSingleChatDB() {
        return SpringContext.getBean(SingleChatDB.class);
    }
    public MessageSendLogMapper getMsgSendLogMapper(){return SpringContext.getBean(MessageSendLogMapper.class);}
    public ChatController getchatController() {
       return SpringContext.getBean(ChatController.class);
    }
    public RabbitTemplate getRabbitTemplate() {
       return SpringContext.getBean(RabbitTemplate.class);
    }

    private static int onlineCount = 0;

    public static ConcurrentHashMap<String, WebSocketServer> getWebSocketSet() {
        return webSocketSet;
    }

    //多例模式
    private static ConcurrentHashMap<String, WebSocketServer> webSocketSet = new ConcurrentHashMap<>();
    // id , WebSocket
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private static Logger log = LogManager.getLogger(WebSocketServer.class);
    private String id = "";
    /**
     * 连接建立成功调用的方法*/
    void initSession(String id,Session session){
        System.out.println(id+"连接到了聊天区");
        this.session = session;
        this.id = id;//接收到发送消息的人员编号   就是username
        webSocketSet.put(this.id, this);     //加入set中
        addOnlineCount();           //在线数加1
    }

    public void sendMessage(String msg)  {
        System.out.println(msg);
        try {
            this.session.getBasicRemote().sendText(msg);
        }catch (IOException e){
            System.out.println("发送失败");
            System.out.println(e);
        }

    }

    @OnOpen
    public void onOpen(@PathParam(value = "id") String id, Session session) {
        try {
            getMsgSendLogMapper().createSendLogTable();
            System.out.println("创建LOG表成功");
        }catch (Exception e){
            System.out.println("LOG_DB已经存在");
        }
        this.initSession(id,session); //连接上，先建立session（标识好id）
    }
    /**
     * 发送信息给所有人
     * @param msg
     * @throws IOException
     *
     * #####待办
     *
     */
    public void sendtoAll(String msg) {
        msg = msg.replaceAll("\\\\","");
        Message message = JSON.parseObject(msg).toJavaObject(Message.class); //之所以转为java bean是为了存入DB
        for (String key : webSocketSet.keySet()) {
            System.out.println(key+"将会收到消息:");
            System.out.println(msg);
            webSocketSet.get(key).sendMessage(msg); //要发json str 直接发
        }
        System.out.println("群发完毕");
    }


    /**
     * 发送信息给指定ID用户，如果用户不在线则返回不在线信息给自己
     * @param msg
     * @param id
     * @throws IOException
     */
    public void sendtoUser(String msg,String id) throws IOException {
        System.out.println("sendgtouser msg:"+msg);
        msg = msg.replaceAll("\\\\","");
        System.out.println(msg);
//      不能有 去除两头的，否则不i是jason
        Message message = JSON.parseObject(msg).toJavaObject(Message.class);

        if (webSocketSet.get(id) != null) {
            webSocketSet.get(id).sendMessage(msg);
        } else {
            //如果用户不在线则让MQ丢到队列中即可
            JSONObject json  = new JSONObject();
            json.put("message","用户不在线");
            json.put("sender","System");
            json.put("receiver",message.getSender());
            json.put("sendTimeStamp",Long.toString(System.currentTimeMillis()));
            sendtoUser(json.toJSONString(),id);
        }

    }
    /**
     * 收到客户端消息后调用的方法
     *
     * @param msg 客户端发送过来的消息*/
    @OnMessage
    @SneakyThrows
    public void onMessage(String msg, Session session) {
        System.out.println("websocket监听到了一条消息:"+msg);
     //   Message message = (Message) Json2Obj.json2OBJ(msg,Message.class);
          Message message = JSONObject.parseObject(msg.replaceAll("\\\\","")).toJavaObject(Message.class);
      //  System.out.println(message.getSender()+"发送了一条消息给"+message.getReceiver()+"内容是："+message.getMessage());

        if (message.getReceiver().equals("group")){
            sendtoAll(msg);//一律群发
            HashMap<String,String> hashMap = new HashMap<>();

            hashMap.put("sender",message.getSender());
            hashMap.put("message",message.getMessage());
            hashMap.put("reveiver","group");
            System.out.println(hashMap);
            ChatWith chatWith = new ChatWith(message.getSender(),message.getReceiver());
            org.springframework.amqp.core.Message sendmsg = MessageBuilder.withBody(msg.getBytes()).build();
            getRabbitTemplate().send(RabbitConfig.EXCHANGE_TOPIC,"group",
                    sendmsg,new CorrelationData(message.getSendTimeStamp()));
            //getRabbitConsumer().recvGroupMsg(hashMap);
            //getchatController().sendGroupMsg(hashMap);
            System.out.println("msg的时间戳:"+message.getSendTimeStamp());


        }else{
            try {
                sendtoUser(msg,message.getReceiver());
                sendtoUser(msg,message.getSender());
                //getSingleChatDB().sendSingleChatMessage(message); //存入数据库
                //getRabbitConsumer().recvSingleMsg(message);
                ChatWith chatWith = new ChatWith(message.getSender(),message.getReceiver());
                System.out.println("MQ开始发送单聊消息");
                org.springframework.amqp.core.Message sendmsg = MessageBuilder.withBody(msg.getBytes()).build();
                    getRabbitTemplate().send(RabbitConfig.EXCHANGE_TOPIC,"single",
                            sendmsg,new CorrelationData(message.getSendTimeStamp()));
                System.out.println("msg的时间戳:"+message.getSendTimeStamp());
                System.out.println("MQ结束发送单聊消息");

               // getRabbitTemplate().send(RabbitConfig.SINGLE_CHAT_QUEUE_NAME,RabbitConfig.SINGLE_CHAT_QUEUE_NAME,message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this.id);  //从set中删除
        subOnlineCount();           //在线数减1
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }
    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

}