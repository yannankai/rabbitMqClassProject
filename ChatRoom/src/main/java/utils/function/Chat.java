package utils.function;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Chat {
    private static Socket socket;
    private static BufferedWriter bufferedWriter;
    private static BufferedReader bufferedReader;
    private static final String ENDMARK = "*--OVER--*";
    @SneakyThrows
    public Chat(Socket initSocket){
        socket = initSocket;
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @SneakyThrows
    public static void sendMessage(){
        Scanner in  = new Scanner(System.in);
        System.out.println("请输入要发送的信息(结束输入消息，请输入end):");

        while(true){
            String str = in.next();
            if (str.equals("end")){
                bufferedWriter.write(ENDMARK);bufferedWriter.newLine();
                bufferedWriter.flush(); //over作为结束标记
                break;
            }
            bufferedWriter.write(str);bufferedWriter.newLine();
            bufferedWriter.flush(); //over作为结束标记
        }
    }
    @SneakyThrows
    public static void recvMessage(){
        System.out.println("以下是收到的信息:");
        while (true){
            try {
                String str = bufferedReader.readLine();
                if (str.equals(ENDMARK)) break;
                System.out.println(str);
            }catch (Exception e){
                System.out.println("异常退出聊天");
                break;
            }
        }
    }

}
