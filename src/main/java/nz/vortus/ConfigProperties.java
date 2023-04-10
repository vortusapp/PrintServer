package nz.vortus;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties {

    private static final Properties props = new Properties();

    static {
        String filePath = "src/main/java/nz/vortus/config.properties";
        try (InputStream in = new FileInputStream(filePath)) {
            props.load(in);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static int getServerPort(){
        return Integer.parseInt(props.getProperty("server.port"));
    }

    public static String getServerAPIKey(){
        return props.getProperty("server.apikey");
    }

    public static String getKafkaPrintQueueTopic(){
        return props.getProperty("kafka.printqueue.topic");
    }
}
