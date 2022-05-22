package ma.octo.demoksqlwebsocket.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.confluent.ksql.api.client.Row;
import lombok.RequiredArgsConstructor;
import ma.octo.demoksqlwebsocket.exception.TechnicalException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AggregatorUtils {

  private final ObjectMapper mapper;

  public <T> String rowToString(Row row, Class<T> t) {
    Field[] declaredFields = t.getDeclaredFields();
    Map<String, Object> data = new HashMap<>();
    for (Field declaredField : declaredFields) {
      Object filed = row.getValue(declaredField.getName().toUpperCase());
      data.put(declaredField.getName(), filed);
    }
    String dataToSend = "";
    try {
      dataToSend = mapper.writeValueAsString(data);
    } catch (JsonProcessingException e) {
      throw new TechnicalException(e.getMessage());
    }
    return dataToSend;
  }

}
