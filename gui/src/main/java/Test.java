import util.ConfigHandle;

import java.io.File;

public class Test {

    public static void main(String[] args) {
        ConfigHandle handle = new ConfigHandle();
        File[] configList = handle.ConfigScan("gui/src/main/res/config/filter");

        for (File s : configList) {
            System.out.println(s);
        }
    }
}
