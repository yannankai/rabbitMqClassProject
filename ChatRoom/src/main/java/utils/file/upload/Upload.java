package utils.file.upload;

import java.io.File;
import java.net.Socket;

public interface Upload {
    //客户端提供文件路径，将文件内容写入socket的outputstream中
    void uploadCharFile(File file,Socket socket);
    void uploadBinFile(File file,Socket socket);
    void uploadFile(Socket socket,String filePath);
}
