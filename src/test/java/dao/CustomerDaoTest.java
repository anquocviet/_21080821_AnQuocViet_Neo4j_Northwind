package dao;

import fit.se.dao.CustomerDao;
import fit.se.utils.AppUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDaoTest {
   private final String dbName = "neo4j";
   private CustomerDao customerDao;

   @BeforeEach
   void setUp() {
      customerDao = new CustomerDao(AppUtils.initDriver(), dbName);
   }

   @Test
   void testCountOrderByCustomer() {
      customerDao.countOrderByCustomer()
            .forEach((k, v) -> System.out.println(k.getCompanyName() + " - " + v));
      assertEquals(89, customerDao.countOrderByCustomer().size());
   }

   @Test
   void countTotalProductSold() {
      customerDao.countTotalProductSold()
            .forEach((k, v) -> System.out.println(k + " - " + v));
      assertEquals(77, customerDao.countTotalProductSold().size());
   }

   @Test
   void calcTotalPriceByOrderDate() {
      LocalDateTime date = LocalDateTime.of(1996, 7, 4, 0, 0, 0);
      customerDao.calcTotalPriceByOrderDate(date)
            .forEach((k, v) -> System.out.println(String.format("Ngày: %s\nTổng tiền: %s", k, v)));
   }

   @Test
   void sumTotalPriceByMonthYear() {
      int month = 7, year = 1996;
      System.out.println(customerDao.sumTotalPriceByMonthYear(month, year));
   }


   @AfterEach
   void tearDown() {
      customerDao.close();
   }
}