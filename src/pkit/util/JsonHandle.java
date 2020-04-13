package pkit.util;

import com.esotericsoftware.kryo.util.ObjectMap;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.jetbrains.annotations.NotNull;
import pkit.core.base.config.Config;
import pkit.core.base.packet.TcpPPacket;

import java.io.File;
import java.io.IOException;

public class JsonHandle {

    private final JsonMapper mapper = new JsonMapper();

    // todo 添加更多函数，不一定要写入文件，还可以生成 json 对象
    public void Object2Json(@NotNull File jsonFile, @NotNull Object object) throws IOException {
        this.mapper.writeValue(jsonFile, object);
    }

    public Object Json2Object(@NotNull File jsonFile, @NotNull Class<TcpPPacket> clazz) throws IOException {
        return this.mapper.readValue(jsonFile, clazz);
    }
}
