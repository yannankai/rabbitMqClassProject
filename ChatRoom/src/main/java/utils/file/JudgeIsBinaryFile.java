package utils.file;

import java.io.File;
import java.io.FileInputStream;

public class JudgeIsBinaryFile {
    public static boolean isBinary(File file)
    {
        boolean isBinary = false;
        try {
            FileInputStream fin = new FileInputStream(file);
            long len = file.length();
            for (int j = 0; j < (int) len; j++) {
                int t = fin.read();
                if (t < 32 && t != 9 && t != 10 && t != 13) {//不可显示字符，且非\r,\n,空格    大概率是binary
                    isBinary = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isBinary;
    }

}
