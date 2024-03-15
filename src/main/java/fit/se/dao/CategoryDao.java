package fit.se.dao;

import fit.se.entity.Category;
import fit.se.utils.AppUtils;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.types.Node;

import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: vie
 * @date: 12/03/2024
 */
public class CategoryDao {
   private Driver driver;
   private SessionConfig sessionConfig;

   public CategoryDao(Driver driver, String dbName) {
      this.driver = driver;
      sessionConfig = SessionConfig
                            .builder()
                            .withDefaultAccessMode(AccessMode.WRITE)
                            .withDatabase(dbName)
                            .build();
   }

   /**
    * Get top 10 categories
    *
    * @return List<Category>
    */
   public List<Category> getTop10Categories() {
      String query = "MATCH (c:Category) RETURN c LIMIT 10";
      try (Session session = driver.session(sessionConfig)) {
         return session.executeRead(tx -> {
            Result result = tx.run(query);
            if (!result.hasNext()) {
               return null;
            }
            return result.stream()
                         .map(record -> {
                            Node node = record.get("c").asNode();
                            return AppUtils.convert(node, Category.class);
                         })
                         .toList();
         });
      }
   }

   /**
    * Get category by ID
    *
    * @param id
    * @return
    */
   public Category getCategoryByID(String id) {
      String query = "MATCH (c:Category {categoryID: $id}) RETURN c";
      Map<String, Object> params = Map.of("id", id);
      try (Session session = driver.session(sessionConfig)) {
         return session.executeRead(tx -> {
            Result result = tx.run(query, params);
            if (!result.hasNext()) {
               return null;
            }
            Record record = result.single();
            Node node = record.get("c").asNode();
            return AppUtils.convert(node, Category.class);
         });
      }
   }

   /**
    * Add category
    *
    * @param category
    */
   public void addCategory(Category category) {
      String query = "CREATE (c:Category {categoryID: $id, categoryName: $name, description: $description})";
      Map<String, Object> params = Map.of(
            "id", category.getId(),
            "name", category.getName(),
            "description", category.getDescription()
      );
      try (Session session = driver.session(sessionConfig)) {
         session.executeWrite(tx -> tx.run(query, params).consume());
      }
   }

   /**
    * Update category
    *
    * @param category
    * @return boolean
    */
   public boolean updateCategory(Category category) {
      String query = "MATCH (c:Category {categoryID: $id}) SET c.categoryName = $name, c.description = $description";
      Map<String, Object> params = Map.of(
            "id", category.getId(),
            "name", category.getName(),
            "description", category.getDescription()
      );
      try (Session session = driver.session(sessionConfig)) {
         return session.executeWrite(tx -> {
            Result result = tx.run(query, params);
            return result.consume().counters().nodesCreated() > 0;
         });
      }
   }

   /**
    * Remove category by ID
    *
    * @param id
    * @return boolean
    */
   public boolean removeCategoryByID(String id) {
      String query = "MATCH (c:Category {categoryID: $id}) DETACH DELETE c";
      Map<String, Object> params = Map.of("id", id);
      try (Session session = driver.session(sessionConfig)) {
         return session.executeWrite(tx -> {
            Result result = tx.run(query, params);
            return result.consume().counters().nodesDeleted() > 0;
         });
      }
   }

   public void close() {
      driver.close();
      driver = null;
   }
}
