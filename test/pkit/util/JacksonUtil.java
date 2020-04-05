package pkit.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import pkit.core.base.config.CaptureNetworkInterfaceConfig;

import java.io.File;
import java.io.IOException;
import java.util.Date;


public class JacksonUtil {

    public static void main(String[] args) throws IOException {
        System.out.println("Hello World!");
        CaptureNetworkInterfaceConfig config = new CaptureNetworkInterfaceConfig();
        config.setId(1);
        config.setName("nif config");
        config.setComment("this is the nif config");
        config.setTimestamp(new Date());
        config.Initial();

        ObjectMapper mapper = new ObjectMapper();

        String filepath = "config.json";
        File configFile = new File(filepath);
        mapper.writeValue(configFile, config);

        CaptureNetworkInterfaceConfig config1 = mapper.readValue(configFile, CaptureNetworkInterfaceConfig.class);
        System.out.println(config1.getTimestamp());
    }
}
