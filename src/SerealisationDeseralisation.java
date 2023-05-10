
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class SerealisationDeseralisation {
     public static List<Product> deserialize(byte[] message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(message, new TypeReference<List<Product>>(){});

    }
    public static String serialize(List<Product> productList) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(productList);
    }
}