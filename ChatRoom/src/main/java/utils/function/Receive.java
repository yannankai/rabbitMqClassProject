package utils.function;

import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;

public class Receive {
    private static Socket socket;
    private static InputStream inputStream;
    private static OutputStream outputStream;
    private static BufferedReader bufferedReader;
    private static BufferedWriter bufferedWriter;
    private static final String ENDMARK = "*--OVER--*";
    @SneakyThrows
    public Receive(Socket theSocket){
        socket = theSocket;
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @SneakyThrows
    public int recNums(){
        return Integer.parseInt(bufferedReader.readLine());
    }

    @SneakyThrows
    public String recWords(){
        StringBuilder str = new StringBuilder();
        while(true){
            String cur = bufferedReader.readLine();
            System.out.println(cur);
            if (cur.equals(ENDMARK)) break;
            if (cur==null) break;//没读到，直接break

            else{
                str.append(cur) ;
                str.append("\n");
            }
        }
//        System.out.println("POSITION:recWords(接收传来的filename str.toString()):"+str.toString());
        return str.toString();
    }



//    @SneakyThrows
//    public String recFileStream(){ //只在文件传输才会使用stream
//        byte [] buf = new byte[1024];
//        int len =0;
//        while ((len=inputStream.read(buf))!=-1){
//
//        }
//    }

}
