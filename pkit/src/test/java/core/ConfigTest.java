//package core;
//
//import com.fasterxml.jackson.databind.JavaType;
//import com.fasterxml.jackson.databind.json.JsonMapper;
//import gui.model.config.FilterProperty;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ConfigTest {
//
//    public static void main(String[] args) throws IOException {
//
//        JsonMapper mapper = new JsonMapper();
//
//        FilterProperty f1 = new FilterProperty();
//        FilterProperty f2 = new FilterProperty();
//
//        f2.setName("ttt");
//
//        List<FilterProperty> properties = new ArrayList<>();
//        properties.add(f1);
//        properties.add(f2);
//
//        mapper.writeValue(new File("tmp/f.json"), properties);
//
//        JavaType jt = mapper.getTypeFactory().constructParametricType(ArrayList.class, FilterProperty.class);
//        List<FilterProperty> urlist = mapper.readValue(new File("tmp/f.json"), jt);
//        System.out.println(urlist.get(1).getName());
//    }
//}
