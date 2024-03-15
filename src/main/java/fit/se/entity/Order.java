package fit.se.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * @description
 * @author: vie
 * @date: 11/03/2024
 */

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Order {
   @JsonProperty("orderID")
   private String id;
   private String shipCity;
   private String freight;
   private String requiredDate;

   private String employeeID;
   private String shipName;
   private String shipPostalCode;
   private String shipCountry;
   private String shipAddress;
   private String shipVia;
   private LocalDate shippedDate;
   private LocalDate orderDate;
   private String shipRegion;

   @ToString.Exclude
   private Customer customer;

   public Order(String id, String shipCity, String freight, String requiredDate, String employeeID, String shipName, String shipPostalCode, String shipCountry, String shipAddress, String shipVia, LocalDate shippedDate, LocalDate orderDate, String shipRegion) {
      this.id = id;
      this.shipCity = shipCity;
      this.freight = freight;
      this.requiredDate = requiredDate;
      this.employeeID = employeeID;
      this.shipName = shipName;
      this.shipPostalCode = shipPostalCode;
      this.shipCountry = shipCountry;
      this.shipAddress = shipAddress;
      this.shipVia = shipVia;
      this.shippedDate = shippedDate;
      this.orderDate = orderDate;
      this.shipRegion = shipRegion;
   }
}
