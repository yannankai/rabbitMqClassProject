package configuration;



import lombok.SneakyThrows;

import java.io.FileReader;
import java.util.Properties;

public class ReadConfig {
    private static final String filepath = "C:\\junior_2\\中间件\\大作业\\middle-ware-repos\\ChatRoom\\src\\main\\resources\\basicConfig.properties";
    @SneakyThrows
    public static Properties readConfigInfo() {
        Properties pro = new Properties();
        pro.load(new FileReader(filepath));
        return pro;
    }
}
