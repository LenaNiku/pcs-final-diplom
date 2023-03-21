import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

import java.util.List;

public class JsonWrapper {
    public static String listValueToJson(List<PageEntry> listValue) {
        return new Gson().toJson(listValue);
    }
}
