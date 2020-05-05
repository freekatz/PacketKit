package util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.json.JsonMapper;
import gui.model.Property;
import gui.model.history.CapturePcapFileHistoryProperty;
import gui.model.history.FilterHistoryProperty;

import java.io.File;
import java.io.IOException;
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

    public static void AddHistory(String path, String history, Class clazz) {
        JsonMapper mapper = new JsonMapper();
        try {
            if (clazz.equals(FilterHistoryProperty.class)) {
                FilterHistoryProperty property = mapper.readValue(new File(path), FilterHistoryProperty.class);
                property.getHistory().add(history);
                mapper.writeValue(new File(path), property);
            }
            else if (clazz.equals(CapturePcapFileHistoryProperty.class)) {
                CapturePcapFileHistoryProperty property = mapper.readValue(new File(path), CapturePcapFileHistoryProperty.class);
                property.getHistory().add(history);
                mapper.writeValue(new File(path), property);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void RemoveHistory(String path, String history, Class clazz) {

        // filter box and pcap file list add the remove opt
        JsonMapper mapper = new JsonMapper();
        try {
            if (clazz.equals(FilterHistoryProperty.class)) {
                FilterHistoryProperty property = mapper.readValue(new File(path), FilterHistoryProperty.class);
                property.getHistory().remove(history);
                mapper.writeValue(new File(path), property);
            }
            else if (clazz.equals(CapturePcapFileHistoryProperty.class)) {
                CapturePcapFileHistoryProperty property = mapper.readValue(new File(path), CapturePcapFileHistoryProperty.class);
                property.getHistory().remove(history);
                mapper.writeValue(new File(path), property);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List ReadConfig(String path, Class clazz) {
        // 读取配置列表
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
        List<Property> list = FileHandle.ReadConfig(path, property.getClass());
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
        List<Property> list = FileHandle.ReadConfig(path, property.getClass());
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
