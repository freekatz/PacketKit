//import com.fasterxml.jackson.databind.json.JsonMapper;
//import gui.model.history.FilterHistoryProperty;
//import gui.model.history.CapturePcapFileHistoryProperty;
//
//import java.io.IOException;
//
//public class SetTest {
//
//    public static void main(String[] args) throws IOException {
//
//
//        JsonMapper mapper = new JsonMapper();
//
//        FilterHistoryProperty filterHistoryProperty = new FilterHistoryProperty();
//        CapturePcapFileHistoryProperty capturePcapFileHistoryProperty = new CapturePcapFileHistoryProperty();
//
//        filterHistoryProperty.getHistory().add("arp");
//        filterHistoryProperty.getHistory().add("tcp");
//
//        String js = mapper.writeValueAsString(filterHistoryProperty);
//
//        System.out.println(js);
//
//    }
//}
