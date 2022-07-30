package utils.file.receive;

import java.io.File;
import java.net.Socket;

public interface ReceiveInterface {
    //客户端传使用socket传来二进制流，服务端接收并生成同名文件于默认目录
    void recBinFile(File file,Socket socket);
    //客户端传来字符文件
    void recFile(Socket socket);
    void recCharFile(File file,Socket socket);
}
