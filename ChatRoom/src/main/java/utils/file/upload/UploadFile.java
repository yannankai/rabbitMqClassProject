package utils.file.upload;

import lombok.SneakyThrows;
import utils.ReleaseSource;
import utils.file.JudgeIsBinaryFile;
import utils.function.Send;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
/*
    使用多态（建议）
* 流：用OutputStream ,InputStream （引用）  去定义 BufferedInputStream,BUfferedOutputStream（实体）去传输流
* 字符，用Reader，Writer 引用，去定义BufferedWriter,BufferedReader 传输字符
* 如果需要子类的功能（譬如readLine，不得不用子类的引用，则使用子类引用
* */
public class UploadFile implements Upload{
    private static Send send;
    private static final String ENDMARK = "*--OVER--*";

    @SneakyThrows
    @Override
    public void uploadFile(Socket socket,String filePath){
        send = new Send(socket);
        File file = new File(filePath);
        boolean isBinary = JudgeIsBinaryFile.isBinary(file);
        uploadFileName(file,socket);
        if (isBinary){
            uploadBinFile(file,socket);
        }else{
            uploadCharFile(file,socket);
        }

    }


    @Override
    @SneakyThrows
    public void uploadCharFile(File file,Socket socket) {

        BufferedReader fromFile = new BufferedReader(new FileReader(file));
        BufferedWriter toSocket = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        String contentLine ;
        while ((contentLine = fromFile.readLine())!=null){
            toSocket.write(contentLine);
            System.out.println("本次写入socket的内容数为:"+contentLine);
        }
        System.out.println("写入完毕");
        ReleaseSource.releaseSocket(socket,fromFile,toSocket);
    }

    @Override
    @SneakyThrows
    public void uploadBinFile(File file,Socket socket) {
       uploadFileName(file,socket);
       binFile2Socket(file,socket);
    }


    @SneakyThrows
    public void uploadFileName(File file,Socket socket) {
        String fileName = file.getName();//传输文件名
        send.sendChars(fileName);
    }
    @SneakyThrows
    public void binFile2Socket(File file,Socket socket){
        //传输文件内容
        InputStream fileIn = new FileInputStream(file);
        OutputStream out = new BufferedOutputStream(socket.getOutputStream());
        byte[] buf = new byte[1000000000];
        int len=0;
        while((len=fileIn.read(buf))!=-1){
            out.write(buf);
        }
        System.out.println("二进制写完了");
//        ReleaseSource.releaseSocket(socket,fileIn,out);
    }
}
