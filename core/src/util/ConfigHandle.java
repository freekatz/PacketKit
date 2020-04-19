package util;

import java.io.File;
import java.io.FilenameFilter;

public class ConfigHandle {

    public static File[] ConfigScan(String dirPath) {
        File dir = new File(dirPath);
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept
                    (File dir, String name) {
                return name.endsWith("json");
            }
        };
        File[] configList = dir.listFiles();
        if (configList==null || configList.length == 0)
            return new File[0];
        return configList;
    }
}
