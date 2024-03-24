package fit.se.dao;

import fit.se.entity.Product;
import fit.se.utils.AppUtils;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description
 * @author: vie
 * @date: 15/3/24
 */
public class ProductDao {
   private Driver driver;
   private SessionConfig sessionConfig;

   public ProductDao(Driver driver, String dbName) {
      this.driver = driver;
      sessionConfig = SessionConfig
                            .builder()
                            .withDefaultAccessMode(AccessMode.WRITE)
                            .withDatabase(dbName)
                            .build();
   }

   /**
    * Find products by name of supplier
    *
    * @param supplierName
    * @return List<Product>
    */
   public List<Product> findProductsBySupplierName(String supplierName) {
      String query = "MATCH (s:Supplier {companyName: $supplierName})-[r:SUPPLIES]->(p:Product) RETURN p";
      Map<String, Object> params = Map.of("supplierName", supplierName);
      try (Session session = driver.session(sessionConfig)) {
         return session.executeRead(tx -> {
            Result result = tx.run(query, params);
            if (!result.hasNext()) {
               return null;
            }
            return result.stream()
                         .map(record -> record.get("p").asNode())
                         .map(node -> AppUtils.convert(node, Product.class))
                         .toList();
         });
      }
   }

   /**
    * Find products have unit price largest
    *
    * @return List<Product>
    */
   public List<Product> findProductsHaveUnitPriceLargest() {
      String query = "MATCH (p:Product) WITH MAX(p.unitPrice) AS maxPrice " +
                           "MATCH (p:Product {unitPrice: maxPrice}) RETURN p";
      try (Session session = driver.session(sessionConfig)) {
         return session.executeRead(tx -> {
            Result result = tx.run(query);
            if (!result.hasNext()) {
               return null;
            }
            return result.stream()
                         .map(record -> record.get("p").asNode())
                         .map(node -> AppUtils.convert(node, Product.class))
                         .toList();
         });
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }

   /**
    * Count customers by city
    *
    * @return Map<String, Integer>
    */
   public Map<String, Integer> countCustomersByCity() {
      String query = "MATCH (c:Customer) RETURN c.city, COUNT(c) AS count ORDER BY count, c.city";
      try (Session session = driver.session(sessionConfig)) {
         return session.executeRead(tx -> {
            Result result = tx.run(query);
            if (!result.hasNext()) {
               return null;
            }
            return result.stream()
                         .collect(Collectors.toMap(
                               record -> record.get("c.city").asString(),
                               record -> record.get("count").asInt(),
                               (x, y) -> y,
                               LinkedHashMap::new
                         ));
         });
      }
   }

   /**
    * Calc total price of bill by orderID
    * @param orderID
    * @return double
    */
   public double calcTotalPriceByOrderID(String orderID) {
      String query = "MATCH (o:Order {orderID: $orderID})-[r:ORDERS]->(p:Product) " +
                           "RETURN sum(toInteger(r.unitPrice) * r.quantity) AS totalPrice";
      Map<String, Object> params = Map.of("orderID", orderID);
      try (Session session = driver.session(sessionConfig)) {
         return session.executeRead(tx -> {
            Result result = tx.run(query, params);
            if (!result.hasNext()) {
               return 0.0;
            }
            return result.single().get("totalPrice").asDouble();
         });
      }
   }


   public void close() {
      driver.close();
      driver = null;
   }
}
