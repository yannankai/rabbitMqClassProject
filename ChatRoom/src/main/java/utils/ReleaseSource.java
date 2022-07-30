package utils;

import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;

public class ReleaseSource {
    @SneakyThrows
    public static void releaseSocket(Socket socket, Reader inputStream, Writer outputStream){
        socket.close();
        inputStream.close();
        outputStream.close();
    }
    @SneakyThrows
    public static void releaseSocket(Socket socket, Writer outputStream){
        socket.close();
        outputStream.close();
    }
    @SneakyThrows
    public static void releaseSocket(Socket socket, Reader inputStream){
        socket.close();
        inputStream.close();
    }
    //释放 socket
    @SneakyThrows
    public static void releaseSocket(Socket socket){
        socket.close();
    }
    @SneakyThrows
    public static void releaseSocket(Socket socket, OutputStream outputStream){
        outputStream.close();
        socket.close();
    }
    @SneakyThrows
    public static void releaseSocket(Socket socket, InputStream inputStream){
        inputStream.close();
        socket.close();
    }
    @SneakyThrows
    public static void releaseSocket(Socket socket,InputStream inputStream,OutputStream outputStream){
        socket.close();
        inputStream.close();
        outputStream.close();
    }

    @SneakyThrows
    public static void releaseSocket(Socket socket, Reader inputStream, OutputStream outputStream){
        socket.close();
        inputStream.close();
        outputStream.close();
    }
    @SneakyThrows
    public static void releaseSocket(Socket socket,InputStream inputStream,Writer outputStream){
        socket.close();
        inputStream.close();
        outputStream.close();
    }

}
