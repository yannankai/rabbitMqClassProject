package utils.file.receive;

import configuration.ReadConfig;
import lombok.SneakyThrows;
import utils.ReleaseSource;
import utils.SpaceEliminate;
import utils.file.JudgeIsBinaryFile;
import utils.function.Receive;

import java.io.*;
import java.net.Socket;

public class ReceiveFile implements ReceiveInterface{
    private static Receive receive;
    private static final String ENDMARK = "*--OVER--*";

    private static final String FILESAVEPATH = ReadConfig.readConfigInfo().getProperty("defaultReceiveFileSavePosition");
    private static void init(Socket socket){
        receive = new Receive(socket);
    }
    @SneakyThrows
    @Override
    public void recFile(Socket socket) {
        init(socket);
//        toClient = socket.getOutputStream();
//        toClient.write("请输入要上传的文件绝对路径:".getBytes(StandardCharsets.UTF_8));
//        toClient.close();

        File file = createFile(socket);
        boolean isBinary = JudgeIsBinaryFile.isBinary(file);
        //创建（传来的文件名含后缀名），判断是什么类型的文件
        if (isBinary){
            System.out.println("开始接收二进制文件");
            recBinFile(file,socket);
            System.out.println("二进制文件写入完成");
        }else{
            System.out.println("开始接受文本文件");
            recCharFile(file,socket);
            System.out.println("文本文件传输完毕");
        }
    }

    @SneakyThrows
    public void recBinFile(File file,Socket socket) {
        byte[] buf = new byte[1024];
        int len=0;

        //读取文件内容
        BufferedInputStream fromClient = new BufferedInputStream(socket.getInputStream());
        OutputStream toFile = new BufferedOutputStream(new FileOutputStream(file));
//        int len = (int)file.length();
//        System.out.println(len);
//        byte[] buf = new byte[len];
//        toFile.write(buf);
        while((len=fromClient.read(buf))!=-1){
            toFile.write(buf);
        }
        System.out.println("内容传输完成");
        ReleaseSource.releaseSocket(socket,fromClient,toFile);
    }



    @SneakyThrows
    @Override
    public void recCharFile(File file,Socket socket) {
        //向文件中写入socket中的流数据（转化为string写入）
        Writer toFile = new BufferedWriter(new FileWriter(file));
        Reader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //OutputStream toFile = new FileOutputStream(file);
        //InputStream fromClient = new BufferedInputStream(socket.getInputStream());
        char[] buf = new char[1024];
        int len=0;
        while((len=fromClient.read(buf))!=-1){
//            toFile.write(new String(buf));
            toFile.write(buf);
        }
        System.out.println("文本文件写入完毕");
        ReleaseSource.releaseSocket(socket,fromClient,toFile);
    }

    @SneakyThrows
    public File createFile(Socket socket) {
        String fileName = receive.recWords();
        String filePath = SpaceEliminate.delSpaceAll(FILESAVEPATH+"/"+fileName); //删除\r,\n,\t
//        System.out.printf("第17个字符%c是:%d",filePath.charAt(16),Integer.valueOf(filePath.charAt(16)));
 //       System.out.println("filepath:"+filePath);
        File file = new File(filePath);
        file.createNewFile();
        System.out.println(fileName+"创建完成");
        return file;
    }

}
