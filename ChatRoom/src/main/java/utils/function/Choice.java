package utils.function;

import lombok.SneakyThrows;
import utils.file.upload.Upload;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Choice {//服务端给客户端发消息，在服务端

    private static Receive receive;
    private static Upload upload;
    private static Send send;
    private static Socket socket;//服务器端的socket
    private InputStream fromClient;
    private OutputStream toClient;

    @SneakyThrows
    public Choice(Socket theSocket) {
        socket =  theSocket;
        send = new Send(socket);
        fromClient = new BufferedInputStream(socket.getInputStream());
        toClient = new BufferedOutputStream(socket.getOutputStream());
    }

    @SneakyThrows
    public int entrance(){
        send.sendChars("请输入要进入的服务:\n按1开始上传文件\n按2开始和服务器聊天\n否则，结束程序\n");
        byte[] choice = new byte[1];
        fromClient.read(choice);
        int option = choice[0];
        return option;
    }
}
