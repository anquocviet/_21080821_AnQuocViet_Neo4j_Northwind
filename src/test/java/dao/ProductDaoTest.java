package dao;

import fit.se.dao.ProductDao;
import fit.se.utils.AppUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @description
 * @author: vie
 * @date: 15/3/24
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductDaoTest {
   private final String DB_Name = "neo4j";
   private ProductDao productDao;

   @BeforeAll
   public void setUp() {
      productDao = new ProductDao(AppUtils.initDriver(), DB_Name);
   }

   @Test
   public void testFindProductBySupplierName() {
      productDao.findProductsBySupplierName("Exotic Liquids").forEach(System.out::println);
      assertEquals(3, productDao.findProductsBySupplierName("Exotic Liquids").size());
   }

   @Test
   public void testFindProductsHaveUnitPriceLargest() {
      productDao.findProductsHaveUnitPriceLargest().forEach(System.out::println);
      assertEquals(1, productDao.findProductsHaveUnitPriceLargest().size());
   }

   @Test
   public void testCountCustomersByCity() {
      productDao.countCustomersByCity().forEach((k, v) -> System.out.println(k + " - " + v));
//      assertEquals(21, productDao.countCustomersByCountry().size());
   }

   @Test
   public void testCalcTotalPriceByOrderID() {
      System.out.println(productDao.calcTotalPriceByOrderID("10248"));
   }

   @AfterAll
   public void tearDown() {
      productDao.close();
   }
}
