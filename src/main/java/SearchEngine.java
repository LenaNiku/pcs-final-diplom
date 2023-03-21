import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface SearchEngine {
    String search(String word) throws JsonProcessingException;
}
