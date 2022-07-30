package socket.server;

import configuration.ReadConfig;
import lombok.SneakyThrows;
import utils.ReleaseSource;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
/*
* multiThread server
* */
public class MultiThreadServer {
    private static int PORT = 9999;

    @SneakyThrows
    public static void readInfoFromSocket(InputStream inputStream){
        byte[] buf = new byte[1024];
        int len=0;
        while((len = (inputStream.read(buf)))!=-1){
            System.out.println("客户端发来的数据是:"+new String(buf,0,len));
        }
    }

    @SneakyThrows
    public static void createThread(Socket socket){
      Thread t =   new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                System.out.println(socket.getInetAddress()+" connected");
                //读取socket中的信息
                InputStream inputStream = socket.getInputStream();
                readInfoFromSocket(inputStream);
                //接受数据结束，关闭socket
                ReleaseSource.releaseSocket(socket,inputStream);
                System.out.println("服务关闭");
            }
        });
      t.start();
    }

    @SneakyThrows
    public static void main(String[] args) {
        ServerSocket serverSocket = new ServerSocket(PORT);
        while(true){
            System.out.println("服务端在9999端口监听，等待连接...");
            Socket socket = serverSocket.accept();//监听到request，创建socket
            System.out.println("建立连接，创建线程");
            createThread(socket);
        }

    }
}
