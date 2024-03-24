package fit.se.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.types.Node;

import java.net.URI;
import java.util.Map;

/**
 * @description
 * @author: vie
 * @date: 11/03/2024
 */
public class AppUtils {
   private static final ObjectMapper objectMapper = new ObjectMapper();
   public static Driver initDriver() {
      URI uri = URI.create("neo4j://localhost:7687");
      String user = "neo4j";
      String pass = "anquocviet_203";
      return GraphDatabase.driver(uri, AuthTokens.basic(user, pass));
   }

   public static <T> T convert(Node node, Class<T> clazz) {
      Map<String, Object> properties = node.asMap();
      try {
         String json = objectMapper.writeValueAsString(properties);
         return objectMapper.readValue(json, clazz);
      } catch (JsonProcessingException e) {
         throw new RuntimeException(e);
      }
   }

   /**
    * Convert a node to Customer
    *
    * @param node
    */
}
