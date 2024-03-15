package dao;

import fit.se.dao.CategoryDao;
import fit.se.entity.Category;
import fit.se.utils.AppUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @description
 * @author: vie
 * @date: 12/03/2024
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CategoryDaoTest {
   public static String DB_Name = "neo4j";
   private CategoryDao categoryDao;

   @BeforeAll
   public void setUp() {
      categoryDao = new CategoryDao(AppUtils.initDriver(), DB_Name);
   }

   @Test
   public void testGetTop10Categories() {
      System.out.println(categoryDao.getTop10Categories());
      assertEquals(8, categoryDao.getTop10Categories().size());
   }

   @Test
   public void testGetCategoryByID() {
      System.out.println(categoryDao.getCategoryByID("1"));
      assertEquals("Beverages", categoryDao.getCategoryByID("1").getName());
   }

   @Test
   public void testAddCategory() {
      Category category = new Category("9", "Test", "Test category", "test.jpg");
      categoryDao.addCategory(category);
   }

   @Test
   public void testUpdateCategory() {
      Category category = new Category("9", "Test", "Test category updated", "test.jpg");
      categoryDao.updateCategory(category);
   }

   @Test void testRemoveCategory() {
      categoryDao.removeCategoryByID("9");
   }

   @AfterAll
   public void tearDown() {
      categoryDao.close();
   }

}
