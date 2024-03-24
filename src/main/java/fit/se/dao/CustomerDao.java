package fit.se.dao;

import fit.se.entity.Customer;
import fit.se.utils.AppUtils;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description
 * @author: vie
 * @date: 17/3/24
 */
public class CustomerDao {
   private Driver driver;
   private SessionConfig sessionConfig;

   public CustomerDao(Driver driver, String dbName) {
      this.driver = driver;
      sessionConfig = SessionConfig
                            .builder()
                            .withDefaultAccessMode(AccessMode.WRITE)
                            .withDatabase(dbName)
                            .build();
   }

   /**
    * Count order by customer
    *
    * @return Map<String, Integer>
    */
   public Map<Customer, Integer> countOrderByCustomer() {
      String query = "MATCH (c:Customer)-[:PURCHASED]->(o:Order) RETURN c, count(o) AS order";
      try (Session session = driver.session(sessionConfig)) {
         return session.executeRead(tx -> {
            Result result = tx.run(query);
            if (!result.hasNext()) {
               return null;
            }
            return result.stream()
                         .collect(Collectors.toMap(
                               record -> AppUtils.convert(record.get("c").asNode(), Customer.class),
                               record -> record.get("order").asInt()
                         ));
         });
      }
   }

   /**
    * Count total product sold
    *
    * @return Map<String, Integer>
    */
   public Map<String, Integer> countTotalProductSold() {
      String query = "MATCH ()-[r:ORDERS]->(p:Product) RETURN p.productName AS productName, count(r.orderID) AS total";
      try (Session session = driver.session(sessionConfig)) {
         return session.executeRead(tx -> {
            Result result = tx.run(query);
            if (!result.hasNext()) {
               return null;
            }
            return result.stream()
                         .collect(Collectors.toMap(
                               record -> record.get("productName").asString(),
                               record -> record.get("total").asInt()
                         ));
         });
      }
   }

   //   CREATE FULLTEXT INDEX name FOR (n:Product) ON EACH [n.productName]

   /**
    * Dùng text search để tìm kiếm sản phẩm theo tên sản phẩm.
    *
    * @param productName
    * @return Product
    */
   public Customer findCustomerByName(String productName) {
      String query = "CALL db.index.fulltext.queryNodes('name', $productName) YIELD node RETURN node";
      try (Session session = driver.session(sessionConfig)) {
         return session.executeRead(tx -> {
            Result result = tx.run(query, Map.of("productName", productName));
            if (!result.hasNext()) {
               return null;
            }
            return AppUtils.convert(result.single().get("node").asNode(), Customer.class);
         });
      }
   }

   // Lệnh load lại dữ liệu
   // LOAD CSV WITH HEADERS FROM "file:///orders.csv" AS row
   // MERGE (o:Order {orderID: row.orderID})
   // SET o.orderDate = DateTime(replace(row.orderDate, ' ', 'T'))


   /**
    * Total price of order in a specific date
    *
    * @param date
    * @return Double
    */
   public Map<String, Double> calcTotalPriceByOrderDate(LocalDateTime date) {
      String query = "MATCH (n:Order)-[o:ORDERS]-(p:Product) WHERE n.orderDate = datetime($date) RETURN n.orderDate AS orderDate, SUM(o.quantity) as total";
      try (Session session = driver.session(sessionConfig)) {
         System.out.println(date.toString());
         return session.executeRead(tx -> {
            Result result = tx.run(query, Map.of("date", date));
            if (!result.hasNext()) {
               return null;
            }
            return result.stream()
                         .collect(Collectors.toMap(
                               record -> record.get("orderDate").asZonedDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                               record -> record.get("total").asDouble()
                         ));
         });
      }
   }

   /**
    * Sum total price of order by month, year
    *
    * @param month, year
    * @return double
    */
   public double sumTotalPriceByMonthYear(int month, int year) {
      String query = "MATCH (n:Order)-[o:ORDERS]-(p:Product) WHERE n.orderDate.month = $month AND n.orderDate.year = $year RETURN SUM(o.quantity * toFloat(o.unitPrice)) as total";
      try (Session session = driver.session(sessionConfig)) {
         return session.executeRead(tx -> {
            Result result = tx.run(query, Map.of("month", month, "year", year));
            if (!result.hasNext()) {
               return 0.0;
            }
            return result.single().get("total").asDouble();
         });
      }
   }

   /**
    * Close the driver
    */
   public void close() {
      driver.close();
      driver = null;
   }
}
