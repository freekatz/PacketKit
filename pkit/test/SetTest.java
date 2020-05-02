import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import gui.model.Property;
import gui.model.SettingProperty;
import gui.model.history.FilterHistoryProperty;
import gui.model.history.PcapFileHistoryProperty;
import util.FileHandle;

import java.io.IOException;
import java.util.HashSet;

public class SetTest {

    public static void main(String[] args) throws IOException {


        JsonMapper mapper = new JsonMapper();

        FilterHistoryProperty filterHistoryProperty = new FilterHistoryProperty();
        PcapFileHistoryProperty pcapFileHistoryProperty = new PcapFileHistoryProperty();

        filterHistoryProperty.getHistory().add("arp");
        filterHistoryProperty.getHistory().add("tcp");

        String js = mapper.writeValueAsString(filterHistoryProperty);

        System.out.println(js);

    }
}
