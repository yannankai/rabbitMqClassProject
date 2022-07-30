package socket.server;

import lombok.SneakyThrows;
import utils.ReleaseSource;
import utils.file.receive.ReceiveFile;
import utils.file.receive.ReceiveInterface;
import utils.function.Chat;
import utils.function.Choice;



import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/*
* single thread
* */
public class Server {
    private static Chat chat;
    private static int PORT = 9999;
    private static Choice serve;
    private static ReceiveInterface receive = new ReceiveFile();

    private static void init(Socket socket){
        chat = new Chat(socket);
        Server.serve = new Choice(socket);
    }

    @SneakyThrows
    public static int service(){
        int choice = serve.entrance();
        return choice;
    }

    public static void chatRoom(){
        try {
            chat.recvMessage();
            chat.sendMessage();
        }
        catch (Exception e){
            System.out.println("聊天异常结束");
        }
    }


    @SneakyThrows
    public static void main(String[] args) {
        ServerSocket serverSocket = new ServerSocket(PORT);System.out.println("服务端在9999端口监听，等待连接...");
        Socket socket = serverSocket.accept();//监听到request，创建socket
        init(socket);System.out.println("成功连接，socket已生成:"+socket.getClass());
        //读取socket中的信息
        int option = service();
        switch (option){
            case 1:{//接收来自客户端的文件
                receive.recFile(socket);
                break;
            }
            case 2:{
                chatRoom();
                break;
            } default:{
                System.out.println("并未开通此服务");
                break;
            }
        }
        //接受数据结束，关闭socket
        ReleaseSource.releaseSocket(socket);
        System.out.println("服务端关闭");
    }



}
