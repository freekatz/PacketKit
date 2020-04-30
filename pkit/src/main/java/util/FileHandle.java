package util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import gui.model.FilterProperty;
import gui.model.Property;
import gui.model.SettingProperty;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandle {

    public static File[] FileScan(String dirPath) {
        File dir = new File(dirPath);
//        FilenameFilter filter = new FilenameFilter() {
//            public boolean accept
//                    (File dir, String name) {
//                return name.endsWith("json");
//            }
//        };
        File[] fileList = dir.listFiles();
        if (fileList==null || fileList.length == 0)
            return new File[0];
        return fileList;
    }

    public static void AddLine(String path, String line) {

        // 去除重复记录
        try {
            RandomAccessFile src = new RandomAccessFile(path, "rw");
            int srcLength = (int) src.length();
            byte[] buff = new byte[srcLength];
            src.read(buff, 0, srcLength);
            src.seek(0);
            byte[] bytes = (line + "\n").getBytes();
            src.write(bytes);
            src.seek(bytes.length);
            src.write(buff);
            src.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static List ReadJson(String path, Class clazz) {
        JsonMapper mapper = new JsonMapper();
        JavaType jt = mapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
        try {
            return mapper.readValue(new File(path), jt);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void SaveConfig(String path, Property property) {
        List<Property> list = FileHandle.ReadJson(path, property.getClass());
        assert list != null;
        list.removeIf(p -> p.getName().equals(property.getName()));
        JsonMapper mapper = new JsonMapper();
        File file = new File(path);
        list.add(property);
        try {
            mapper.writeValue(file, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void DeleteConfig(String path, Property property) {
        List<Property> list = FileHandle.ReadJson(path, property.getClass());
        assert list!=null;
        list.removeIf(p -> p.getName().equals(property.getName()));
        JsonMapper mapper = new JsonMapper();
        File file = new File(path);
        try {
            mapper.writeValue(file, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
