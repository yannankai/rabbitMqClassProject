package socket.client;

import configuration.ReadConfig;
import lombok.SneakyThrows;
import utils.ReleaseSource;
import utils.file.receive.ReceiveFile;
import utils.file.upload.Upload;
import utils.file.upload.UploadFile;
import utils.function.Chat;
import utils.function.Receive;
import utils.function.Send;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Scanner;

public class Client {
    private static Chat chat;
    private static Send send;
    private static Receive receive ;
    private static final Properties CONFINFO = ReadConfig.readConfigInfo();
    private static final String SERVERIP = CONFINFO.getProperty("serverIp");
    private static final int SERVERPORT = Integer.parseInt(CONFINFO.getProperty("serverPort"));
    private static Upload upload = new UploadFile();
    private static Scanner in = new  Scanner(System.in);

    public static  void init(Socket socket){
        receive = new Receive(socket);
        send = new Send(socket);
        upload = new UploadFile();
    }
    public static void chatRoom(Socket socket){ //客户端先发送信息。后续可以用超时来交换主动权
        chat = new Chat(socket);
        try {
            chat.sendMessage();
            chat.recvMessage();
        }
        catch (Exception e){
            System.out.println("聊天结束");
        }
    }

    public static int interactWithEntrance(){
        System.out.println(receive.recWords());//先接收信息
        int option = in.nextInt();
        send.sendNums(option);
        return option;
    }

    public static void main(String[] args) throws Exception{
        Socket socket = new Socket(SERVERIP,SERVERPORT);
        System.out.println("连接成功");
        init(socket);
        int option = interactWithEntrance();

        switch (option){
            case 1:{
                System.out.println("请输入要上传文件的绝对路径:");
                String filePath = in.next();
                upload.uploadFile(socket,filePath);
                break;
            } case 2:{
                chatRoom(socket);break;
            } default:{
                System.out.println("没此功能,退出程序");break;
            }
        }

        ReleaseSource.releaseSocket(socket);
        System.out.println("socket关闭");
    }
//    @SneakyThrows
//    public static void main(String[] args) {
//        Socket socket = new Socket(SERVERIP,SERVERPORT);
//        System.out.println("连接成功");
//
//        InputStream fromServer = new BufferedInputStream(socket.getInputStream());
//        byte[] buf = new byte[1024];
//        int len=0;
//        System.out.println("下面开始读服务端传来的信息");
//        while((len=fromServer.read(buf))!=-1){
//            System.out.println(new String(buf));
//        }
//
//        Scanner in = new Scanner(System.in);
//        System.out.println("请输入要传给服务端的服务序号");//这是客户端的界面console
//        byte choice = in.nextByte();
//        OutputStream toServer = new BufferedOutputStream(socket.getOutputStream());
//        toServer.write(choice);
//
//        if (choice==2)
//        while(in.hasNext()){
//            String myWords = in.next();
//            toServer.write(myWords.getBytes(StandardCharsets.UTF_8));
//            byte [] words = new byte[1024];
//             len = 0;
//            while((len=fromServer.read(words))!=-1){
//                String serverWords = new String(words,0,len);
//                System.out.println(serverWords); //传来的话打印到屏幕上
//            }
//        }
//        else if (choice==1){
//            System.out.println("请输入文件路径:");
//            String filePath = in.next();
//            String fileName = filePath.split("/")[-1];
//            toServer.write(fileName.getBytes(StandardCharsets.UTF_8));
//            upload.uploadFile(socket,filePath);
//        }
//        ReleaseSource.releaseSocket(socket,fromServer,toServer);
//        System.out.println("客户端退出");
//    }




}
