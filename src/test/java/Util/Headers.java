package Util;

import org.yaml.snakeyaml.Yaml;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

public class Headers {
    public static Map<String, String> headers;

    static {
        try {
            Yaml yaml = new Yaml();
            headers = yaml.load(new FileInputStream("src/test/resources/testDataResources/Headers.yaml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




