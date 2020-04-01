package pkit.util;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.jetbrains.annotations.NotNull;
import pkit.core.base.config.Config;

import java.io.File;
import java.io.IOException;

public class ConfigHandle {

    private final JsonMapper mapper = new JsonMapper();

    public void Config2Json(@NotNull File jsonFile, @NotNull Config config) throws IOException {
        this.mapper.writeValue(jsonFile, config);
    }

    public Config Json2Config(@NotNull File jsonFile, @NotNull Class<Config> clazz) throws IOException {
        return this.mapper.readValue(jsonFile, clazz);
    }
}
