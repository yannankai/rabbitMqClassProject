package utils.function;

import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Send {//不传输流
    private static Socket socket;
    private static BufferedReader bufferedReader;
    private static BufferedWriter bufferedWriter;
    private static final String ENDMARK = "*--OVER--*";
    @SneakyThrows
    public Send(Socket theSocket){
        socket = theSocket;
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }
    @SneakyThrows
    public static void sendNums(int number){
        bufferedWriter.write(number);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    @SneakyThrows
    public static void sendChars(String words){
        bufferedWriter.write(words);
        bufferedWriter.newLine();//如果不分开，会在同一行
        bufferedWriter.write(ENDMARK);
        bufferedWriter.newLine();
        bufferedWriter.flush(); //必不可少
        System.out.println("服务端发出了");

    }

    @SneakyThrows
    public static void sendInputChars(){
        System.out.println("请输入要传输的信息(end)表示结束本次传输");
        Scanner in = new Scanner(System.in);
       String words = in.next();
       if (!words.equals("end")){
           bufferedWriter.write(words);
           bufferedWriter.newLine();
           bufferedWriter.flush();
       }else{
           bufferedWriter.write(ENDMARK);
           bufferedWriter.newLine();
           bufferedWriter.flush();
       }

    }

}
